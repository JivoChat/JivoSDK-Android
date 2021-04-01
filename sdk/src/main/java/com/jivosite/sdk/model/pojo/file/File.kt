package com.jivosite.sdk.model.pojo.file

import java.io.InputStream

/**
 * Created on 2/18/21.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
data class File(
    val name: String,
    val type: String,
    val extension: String,
    val mimeType: String,
    val inputStream: InputStream? = null,
    val uri: String,
    val size: Long
)