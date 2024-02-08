package com.jivosite.sdk.logger

import java.net.URI

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
sealed class LogMessage {

    object Initial : LogMessage()

    data class LoadConfig(
        val id: Long,
        val ts: Long = System.currentTimeMillis(),
        val url: String
    ) : LogMessage()

    data class Connecting(
        val id: Long,
        val ts: Long = System.currentTimeMillis(),
        val uri: URI
    ) : LogMessage()

    data class Connected(
        val id: Long,
        val ts: Long = System.currentTimeMillis()
    ) : LogMessage()

    data class Received(
        val id: Long,
        val ts: Long = System.currentTimeMillis(),
        val message: String
    ) : LogMessage()

    data class Sent(
        val id: Long,
        val ts: Long = System.currentTimeMillis(),
        val message: String
    ) : LogMessage()

    data class Ping(
        val id: Long,
        val ts: Long = System.currentTimeMillis(),
        val message: String
    ) : LogMessage()

    data class Pong(
        val id: Long,
        val ts: Long = System.currentTimeMillis(),
        val message: String
    ) : LogMessage()

    data class Disconnected(
        val id: Long,
        val ts: Long = System.currentTimeMillis(),
        val code: Int,
        val reason: String
    ) : LogMessage()

    data class Error(
        val id: Long,
        val ts: Long = System.currentTimeMillis(),
        val cause: Throwable
    ) : LogMessage()
}
