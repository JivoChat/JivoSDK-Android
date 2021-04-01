package com.jivosite.sdk.model.repository.upload

import java.util.*

/**
 * Created on 07.12.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */

data class UploadFilesState(
    val files: Map<String, FileState> = Collections.emptyMap()
)

sealed class UploadState {

    data class Uploading(
        val percent: Int
    ) : UploadState()

    data class Error(
        val errorMessage: String
    ) : UploadState()
}

data class FileState(
    val name: String,
    val type: String,
    val size: Long,
    val uri: String,
    val timestamp: Long,
    val uploadState: UploadState,
    val mimeType: String
)
