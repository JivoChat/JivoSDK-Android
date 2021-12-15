package com.jivosite.sdk.support.ext

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.InputStream

/**
 * Created on 18.01.2021.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */

fun InputStream.asRequestBody(contentType: MediaType? = null): RequestBody {
    return object : RequestBody() {
        override fun contentType() = contentType

        override fun writeTo(sink: BufferedSink) {
            source().use { source -> sink.writeAll(source) }
        }
    }
}