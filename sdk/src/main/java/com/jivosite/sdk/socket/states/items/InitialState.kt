package com.jivosite.sdk.socket.states.items

import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.connection.ConnectionState
import com.jivosite.sdk.model.repository.connection.ConnectionStateRepository
import com.jivosite.sdk.socket.JivoWebSocketService
import com.jivosite.sdk.socket.states.DisconnectReason
import com.jivosite.sdk.socket.states.ServiceState
import com.jivosite.sdk.socket.states.ServiceStateContext
import com.jivosite.sdk.support.usecase.SdkConfigUseCase
import javax.inject.Inject

/**
 * Created on 06.09.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class InitialState @Inject constructor(
    stateContext: ServiceStateContext,
    private val service: JivoWebSocketService,
    private val connectionStateRepository: ConnectionStateRepository,
    private val sdkConfigUseCase: SdkConfigUseCase,
) : ServiceState(stateContext) {

    override fun load() {
        stateContext.changeState(LoadConfigState::class.java)
        connectionStateRepository.setState(ConnectionState.LoadConfig)
        sdkConfigUseCase.execute()
    }

    override fun start() {
        service.connect()
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
        logImpossibleAction("stop")
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