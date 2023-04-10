package com.jivosite.sdk.socket.states.items

import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.socket.JivoWebSocketService
import com.jivosite.sdk.socket.states.DisconnectReason
import com.jivosite.sdk.socket.states.ServiceState
import com.jivosite.sdk.socket.states.ServiceStateContext
import com.jivosite.sdk.support.usecase.SdkConfigUseCase
import javax.inject.Inject

/**
 * Created on 05.04.2023.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class LoadConfigState @Inject constructor(
    stateContext: ServiceStateContext,
    private val service: JivoWebSocketService,
    private val sdkConfigUseCase: SdkConfigUseCase,
) : ServiceState(stateContext) {

    override fun load() {
        sdkConfigUseCase.execute()
    }

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
        service.releaseConnectionKeeper()
        service.unsubscribeFromTransmitter()
        service.stopSelf()
    }

    override fun setDisconnected(reason: DisconnectReason) {
        logImpossibleAction("setDisconnected")
    }

    override fun restart() {
        logImpossibleAction("restart")
    }

    override fun error(reason: String) {
        logImpossibleAction("error")
    }

}