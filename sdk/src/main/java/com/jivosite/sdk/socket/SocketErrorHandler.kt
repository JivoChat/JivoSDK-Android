package com.jivosite.sdk.socket

import com.neovisionaries.ws.client.WebSocketError
import com.neovisionaries.ws.client.WebSocketException

/**
 * Created on 19.11.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class SocketErrorHandler(private val cause: WebSocketException?) {

    private var connectionConsumer: (() -> Boolean)? = null
    private var defaultConsumer: (() -> Unit)? = null

    fun connectionError(consumer: () -> Boolean) {
        connectionConsumer = consumer
    }

    fun default(consumer: () -> Unit) {
        defaultConsumer = consumer
    }

    fun handle() {
        val cause = this.cause ?: return

        val consumed = when (cause.error) {
            WebSocketError.SOCKET_CONNECT_ERROR -> connectionConsumer?.invoke() ?: false
            else -> false
        }

        if (!consumed) {
            defaultConsumer?.invoke()
        }
    }
}

fun socketErrorHandler(cause: WebSocketException?, builder: SocketErrorHandler.() -> Unit) {
    val handler = SocketErrorHandler(cause)
    builder(handler)
    handler.handle()
}