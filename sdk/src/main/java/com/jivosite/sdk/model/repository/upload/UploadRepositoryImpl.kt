package com.jivosite.sdk.model.repository.upload

import androidx.lifecycle.LiveData
import com.jivosite.sdk.api.MediaApi
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.pojo.file.File
import com.jivosite.sdk.model.repository.StateRepository
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.network.resource.Resource
import com.jivosite.sdk.network.resource.UploadResource
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
    private val storage: SharedStorage,
) : StateRepository<UploadFilesState>(schedulers, "UploadFilesState", UploadFilesState()), UploadRepository {

    override val observableState: StateLiveData<UploadFilesState>
        get() = _stateLive


    override fun upload(file: File, successfulUnloading: (url: String) -> Unit) {
        setStateUploading(file)

        createRequest(file).loadSilentlyResource {
            progress { }
            progressUpdate { bytesWritten ->
                bytesWritten?.let {
                    val percent = 100 * it / file.size
                    setStateUploading(file, percent.toInt())
                }
            }
            result { successfulUnloading(it) }
            error { setStateError(file.uri, it) }
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

    private fun setStateUploading(file: File, percent: Int = 0) {
        updateStateInRepositoryThread {
            transform { state ->
                val files = HashMap<String, FileState>()
                files.putAll(state.files)

                if (files.isNotEmpty()) {
                    for ((k, v) in files) {
                        if (k == file.uri) {
                            files[file.uri] = v.copy(uploadState = UploadState.Uploading(percent))
                            break
                        } else {
                            files[file.uri] = FileState(
                                file.name,
                                file.type,
                                file.size,
                                file.uri,
                                System.currentTimeMillis() / 1000,
                                UploadState.Uploading(percent),
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
                        UploadState.Uploading(percent),
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
        return UploadResource.Builder(schedulers)
            .getAccess {
                api.getAccessForFile(
                    siteId,
                    widgetId,
                    file.extension,
                    file.mimeType
                )
            }
            .file { file }
            .upload { url, map, body ->
                api.uploadFile(url, map, body)
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

    override fun clear() {
        // Ignore for now
    }
}