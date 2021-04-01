package com.jivosite.sdk.socket.states.items

import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.connection.ConnectionState
import com.jivosite.sdk.model.repository.connection.ConnectionStateRepository
import com.jivosite.sdk.socket.JivoWebSocketService
import com.jivosite.sdk.socket.states.DisconnectReason
import com.jivosite.sdk.socket.states.ServiceState
import com.jivosite.sdk.socket.states.ServiceStateContext
import javax.inject.Inject

/**
 * Created on 06.09.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class InitialState @Inject constructor(
    stateContext: ServiceStateContext,
    private val service: JivoWebSocketService,
    private val connectionStateRepository: ConnectionStateRepository
) : ServiceState(stateContext) {

    override fun start() {
        stateContext.changeState(ConnectingState::class.java)
        connectionStateRepository.setState(ConnectionState.Connecting)
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
}