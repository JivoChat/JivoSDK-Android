package com.jivosite.sdk.model.repository.upload

import com.jivosite.sdk.model.pojo.file.File
import com.jivosite.sdk.network.resource.Resource
import com.jivosite.sdk.support.vm.StateLiveData

/**
 * Created on 2/16/21.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
interface UploadRepository {

    val observableState: StateLiveData<UploadFilesState>

    fun upload(file: File, successfulUnloading: (url: String) -> Unit)

    fun removeFile(contentUri: String)
}