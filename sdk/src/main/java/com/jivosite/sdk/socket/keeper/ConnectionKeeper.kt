package com.jivosite.sdk.socket.keeper

import android.os.Handler
import android.os.Looper
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.logger.Logger
import com.jivosite.sdk.socket.JivoWebSocketService.Companion.REASON_TIMEOUT
import com.neovisionaries.ws.client.WebSocket

/**
 * Created on 06.09.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class ConnectionKeeper(
    private val ws: WebSocket,
    private val pingInterval: Long,
    private val pongInterval: Long,
    private val logger: Logger
) {

    companion object {
        const val PING_MESSAGE = " "
    }

    private val handler: Handler = Handler(Looper.getMainLooper())

    private val pingSender = object : Runnable {
        override fun run() {
            logger.logPing(PING_MESSAGE)

            Jivo.d("PING")
            
            ws.sendText(PING_MESSAGE)
            handler.postDelayed(this, pingInterval)
        }
    }

    private val pongTimeout = Runnable {
        Jivo.d("PONG TIMEOUT")
        ws.disconnect(REASON_TIMEOUT)
    }

    fun resetPing() {
        handler.removeCallbacks(pingSender)
        handler.postDelayed(pingSender, pingInterval)
    }

    fun resetTimeout() {
        handler.removeCallbacks(pongTimeout)
        handler.postDelayed(pongTimeout, pongInterval)
    }

    fun release() {
        handler.removeCallbacks(pingSender)
        handler.removeCallbacks(pongTimeout)
    }
}