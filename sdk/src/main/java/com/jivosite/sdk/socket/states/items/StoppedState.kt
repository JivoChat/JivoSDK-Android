package com.jivosite.sdk.socket.states.items

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.socket.JivoWebSocketService
import com.jivosite.sdk.socket.states.DisconnectReason
import com.jivosite.sdk.socket.states.ServiceState
import com.jivosite.sdk.socket.states.ServiceStateContext
import javax.inject.Inject

/**
 * Created on 26.11.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class StoppedState @Inject constructor(
    stateContext: ServiceStateContext,
    private val service: JivoWebSocketService
) : ServiceState(stateContext) {

    override fun start() {
        logImpossibleAction("start")
    }

    override fun reconnect(force: Boolean) {
        logImpossibleAction("reconnect")
    }

    override fun setConnected() {
        logImpossibleAction("setConnected")
    }

    override fun send(message: SocketMessage) {
        logImpossibleAction("send(SocketMessage)")
    }

    override fun send(message: String) {
        logImpossibleAction("send(String)")
    }

    override fun stop() {
        Jivo.w("Call stop on stopped state, maybe something wrong, just ignore action")
    }

    override fun setDisconnected(reason: DisconnectReason) {
        Jivo.i("Service successfully stopped")
        service.stopSelf()
    }

    override fun restart() {
        logImpossibleAction("restart")
    }
}