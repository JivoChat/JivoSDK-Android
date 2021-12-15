package com.jivosite.sdk.logger

import java.util.concurrent.atomic.AtomicLong
import javax.inject.Inject

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class DebugLogger @Inject constructor(
    private val logsRepository: LogsRepository
) : Logger {

    private val indexer = AtomicLong(0)

    override fun logConnecting() {
        logsRepository.addMessage(
            LogMessage.Connecting(
                id = indexer.getAndIncrement()
            )
        )
    }

    override fun logConnected() {
        logsRepository.addMessage(
            LogMessage.Connected(
                id = indexer.getAndIncrement()
            )
        )
    }

    override fun logReceivedMessage(message: String) {
        logsRepository.addMessage(
            LogMessage.Received(
                id = indexer.getAndIncrement(),
                message = message
            )
        )
    }

    override fun logSentMessage(message: String) {
        logsRepository.addMessage(
            LogMessage.Sent(
                id = indexer.getAndIncrement(),
                message = message
            )
        )
    }

    override fun logPing(message: String) {
        logsRepository.addMessage(
            LogMessage.Ping(
                id = indexer.getAndIncrement(),
                message = message
            )
        )
    }

    override fun logPong(message: String) {
        logsRepository.addMessage(
            LogMessage.Pong(
                id = indexer.getAndIncrement(),
                message = message
            )
        )
    }

    override fun logDisconnected(code: Int, reason: String) {
        logsRepository.addMessage(
            LogMessage.Disconnected(
                id = indexer.getAndIncrement(),
                code = code,
                reason = reason
            )
        )
    }

    override fun logError(cause: Throwable) {
        logsRepository.addMessage(
            LogMessage.Error(
                id = indexer.getAndIncrement(),
                cause = cause
            )
        )
    }
}