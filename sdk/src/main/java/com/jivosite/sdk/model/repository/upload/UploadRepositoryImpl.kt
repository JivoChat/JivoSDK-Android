package com.jivosite.sdk.model.repository.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jivosite.sdk.api.ApiErrors.FILE_TRANSFER_DISABLED
import com.jivosite.sdk.api.MediaApi
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.pojo.file.File
import com.jivosite.sdk.model.repository.StateRepository
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.network.resource.MediaResource
import com.jivosite.sdk.network.resource.Resource
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.ext.loadSilentlyResource
import com.jivosite.sdk.support.vm.StateLiveData
import javax.inject.Inject

/**
 * Created on 2/16/21.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class UploadRepositoryImpl @Inject constructor(
    private val sdkContext: SdkContext,
    private val schedulers: Schedulers,
    private val api: MediaApi,
    private val storage: SharedStorage
) : StateRepository<UploadFilesState>(schedulers, "UploadFilesState", UploadFilesState()), UploadRepository {

    override val observableState: StateLiveData<UploadFilesState>
        get() = _stateLive

    private val _hasLicense = MutableLiveData(false)
    override val hasLicense: LiveData<Boolean>
        get() = _hasLicense

    override fun upload(file: File, successfulUnloading: (url: String) -> Unit) {
        setStateUploading(file)

        createRequest(file).loadSilentlyResource {
            progress { }
            progressUpdate { bytesWritten ->
                bytesWritten?.let {
                    setStateUploading(file, it)
                }
            }
            result { successfulUnloading(it) }
            error {
                setStateError(file.uri, it)
                if (it == FILE_TRANSFER_DISABLED) {
                    updateLicense(false)
                }
            }
        }
    }

    override fun removeFile(contentUri: String) {
        updateStateInRepositoryThread {
            transform { state ->
                val files = HashMap<String, FileState>()
                state.files.filterValues {
                    it.uri != contentUri
                }
                state.copy(files = files)
            }
        }
    }

    override fun updateLicense(hasLicense: Boolean) {
        _hasLicense.value = hasLicense
    }

    private fun setStateUploading(file: File, size: Long = 0) {
        updateStateInRepositoryThread {
            transform { state ->
                val files = HashMap<String, FileState>()
                files.putAll(state.files)

                if (files.isNotEmpty()) {
                    for ((k, v) in files) {
                        if (k == file.uri) {
                            files[file.uri] = v.copy(uploadState = UploadState.Uploading(size))
                            break
                        } else {
                            files[file.uri] = FileState(
                                file.name,
                                file.type,
                                file.size,
                                file.uri,
                                System.currentTimeMillis() / 1000,
                                UploadState.Uploading(size),
                                file.mimeType
                            )
                        }
                    }
                } else {
                    files[file.uri] = FileState(
                        file.name,
                        file.type,
                        file.size,
                        file.uri,
                        System.currentTimeMillis() / 1000,
                        UploadState.Uploading(size),
                        file.mimeType
                    )
                }

                state.copy(files = files)
            }
        }
    }

    private fun createRequest(file: File): LiveData<Resource<String>> {
        val siteId = storage.siteId.toLong()
        val widgetId = storage.widgetId.ifBlank { sdkContext.widgetId }
        return MediaResource.Builder(schedulers)
            .getAccess {
                api.getSign(
                    file.name,
                    storage.clientId.split(".").first(),
                    siteId,
                    widgetId
                )
            }
            .file { file }
            .upload { metadata, url, body ->
                api.uploadMedia(metadata, url, body)
            }
            .build()
            .asLiveData()
    }

    private fun setStateError(uri: String, error: String) {
        updateStateInRepositoryThread {
            transform { state ->
                val files = HashMap<String, FileState>()
                files.putAll(state.files)
                for ((k, v) in files) {
                    if (k == uri) {
                        files[uri] = v.copy(uploadState = UploadState.Error(error))
                        break
                    }
                }
                state.copy(files = files)
            }
        }
    }

    override fun clear() = updateStateInRepositoryThread {
        transform { UploadFilesState() }
    }
}