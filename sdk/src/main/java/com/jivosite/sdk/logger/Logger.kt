package com.jivosite.sdk.logger

import java.net.URI

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
interface Logger {

    fun logLoadConfig(url: String)

    fun logConnecting(uri: URI)

    fun logConnected()

    fun logReceivedMessage(message: String)

    fun logSentMessage(message: String)

    fun logPing(message: String)

    fun logPong(message: String)

    fun logDisconnected(code: Int, reason: String)

    fun logError(cause: Throwable)
}