package com.jivosite.sdk.logger

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class ReleaseLogger : Logger {

    override fun logConnecting() {
        // Ignore
    }

    override fun logConnected() {
        // Ignore
    }

    override fun logReceivedMessage(message: String) {
        // Ignore
    }

    override fun logSentMessage(message: String) {
        // Ignore
    }

    override fun logPing(message: String) {
        // Ignore
    }

    override fun logPong(message: String) {
        // Ignore
    }

    override fun logDisconnected(code: Int, reason: String) {
        // Ignore
    }

    override fun logError(cause: Throwable) {
        // Ignore
    }
}