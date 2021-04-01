package com.jivosite.sdk.network.retrofit

import okhttp3.RequestBody
import okio.*

/**
 * Created on 18.01.2021.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
typealias CountingRequestListener = (progress: Long) -> Unit

class CountingRequestBody(
    private val delegate: RequestBody,
    private val onProgressUpdate: CountingRequestListener,
) : RequestBody() {

    override fun contentType() = delegate.contentType()

    @Throws(IOException::class)
    override fun contentLength() = delegate.contentLength()

    override fun writeTo(sink: BufferedSink) {
        val countingSink = CountingSink(sink, onProgressUpdate)
        val bufferedSink = countingSink.buffer()

        delegate.writeTo(bufferedSink)

        bufferedSink.flush()
    }

    inner class CountingSink(
        delegate: Sink,
        val onProgressUpdate: CountingRequestListener,
    ) : ForwardingSink(delegate) {

        private var bytesWritten = 0L

        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)

            bytesWritten += byteCount

            onProgressUpdate(bytesWritten)
        }
    }
}