package com.jivosite.sdk.logger

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
interface Logger {

    fun logConnecting()

    fun logConnected()

    fun logReceivedMessage(message: String)

    fun logSentMessage(message: String)

    fun logPing(message: String)

    fun logPong(message: String)

    fun logDisconnected(code: Int, reason: String)

    fun logError(cause: Throwable)
}