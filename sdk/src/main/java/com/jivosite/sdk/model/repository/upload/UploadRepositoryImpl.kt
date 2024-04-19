package com.jivosite.sdk.model.repository.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jivosite.sdk.api.ApiErrors.FILE_TRANSFER_DISABLED
import com.jivosite.sdk.api.MediaApi
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.pojo.file.JivoMediaFile
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
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
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

    override fun upload(jivoMediaFile: JivoMediaFile, successfulUnloading: (url: String) -> Unit) {
        setStateUploading(jivoMediaFile)

        createRequest(jivoMediaFile).loadSilentlyResource {
            progress { }
            progressUpdate { bytesWritten ->
                bytesWritten?.let {
                    setStateUploading(jivoMediaFile, it)
                }
            }
            result { successfulUnloading(it) }
            error {
                setStateError(jivoMediaFile.uri, it)
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

    private fun setStateUploading(jivoMediaFile: JivoMediaFile, size: Long = 0) {
        updateStateInRepositoryThread {
            transform { state ->
                val files = HashMap<String, FileState>()
                if (state.files.isNotEmpty()) {
                    for ((k, v) in state.files) {
                        if (k == jivoMediaFile.uri) {
                            files[jivoMediaFile.uri] = v.copy(uploadState = UploadState.Uploading(size))
                            break
                        } else {
                            files[jivoMediaFile.uri] = FileState(
                                jivoMediaFile.name,
                                jivoMediaFile.type,
                                jivoMediaFile.size,
                                jivoMediaFile.uri,
                                System.currentTimeMillis() / 1000,
                                UploadState.Uploading(size),
                                jivoMediaFile.mimeType
                            )
                        }
                    }
                } else {
                    files[jivoMediaFile.uri] = FileState(
                        jivoMediaFile.name,
                        jivoMediaFile.type,
                        jivoMediaFile.size,
                        jivoMediaFile.uri,
                        System.currentTimeMillis() / 1000,
                        UploadState.Uploading(size),
                        jivoMediaFile.mimeType
                    )
                }

                state.copy(files = files)
            }
        }
    }

    private fun createRequest(jivoMediaFile: JivoMediaFile): LiveData<Resource<String>> {
        val siteId = storage.siteId.toLong()
        val widgetId = storage.widgetId
        return MediaResource.Builder(schedulers)
            .getAccess {
                api.getSign(
                    jivoMediaFile.name,
                    storage.clientId.split(".").first(),
                    siteId,
                    widgetId
                )
            }
            .file { jivoMediaFile }
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