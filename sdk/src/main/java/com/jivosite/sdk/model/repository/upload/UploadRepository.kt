package com.jivosite.sdk.model.repository.upload

import androidx.lifecycle.LiveData
import com.jivosite.sdk.model.pojo.file.File
import com.jivosite.sdk.support.vm.StateLiveData

/**
 * Created on 2/16/21.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
interface UploadRepository {

    val observableState: StateLiveData<UploadFilesState>

    val hasLicense: LiveData<Boolean>

    fun upload(file: File, successfulUnloading: (url: String) -> Unit)

    fun removeFile(contentUri: String)

    fun updateLicense(hasLicense: Boolean)

    fun clear()
}