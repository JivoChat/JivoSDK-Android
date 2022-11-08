package com.jivosite.sdk.socket.states.items

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.model.repository.connection.ConnectionState
import com.jivosite.sdk.model.repository.connection.ConnectionStateRepository
import com.jivosite.sdk.socket.JivoWebSocketService
import com.jivosite.sdk.socket.states.DisconnectReason
import com.jivosite.sdk.socket.states.ServiceState
import com.squareup.moshi.Moshi
import javax.inject.Inject

/**
 * Created on 10.09.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class ConnectedState @Inject constructor(
    stateContext: JivoWebSocketService,
    private val service: JivoWebSocketService,
    private val parser: Moshi,
    private val connectionStateRepository: ConnectionStateRepository,
    private val agentRepository: AgentRepository
) : ServiceState(stateContext) {

    override fun start() {
        logImpossibleAction("start")
    }

    override fun reconnect(force: Boolean) {
        stateContext.changeState(DisconnectedState::class.java)
        connectionStateRepository.setState(ConnectionState.Disconnected(0))
        service.releaseConnectionKeeper()
        service.unsubscribeFromTransmitter()
        stateContext.getState().reconnect(force)
    }

    override fun setConnected() {
        logImpossibleAction("setConnected")
    }

    override fun send(message: SocketMessage) {
        service.send(parser.adapter(SocketMessage::class.java).toJson(message))
    }

    override fun send(message: String) {
        service.send(message)
    }

    override fun stop() {
        stateContext.changeState(StoppedState::class.java)
        connectionStateRepository.setState(ConnectionState.Stopped)
        service.releaseConnectionKeeper()
        service.unsubscribeFromTransmitter()
        service.disconnect()
    }

    override fun setDisconnected(reason: DisconnectReason) {
        stateContext.changeState(DisconnectedState::class.java)
        connectionStateRepository.setState(ConnectionState.Disconnected(0))
        agentRepository.onConnectionStateChanged()
        service.releaseConnectionKeeper()
        service.unsubscribeFromTransmitter()

        when (reason) {
            is DisconnectReason.ChangeInstance -> {
                service.disconnect()
                stateContext.getState().reconnect(true)
            }
            is DisconnectReason.BlackListed -> {
                service.disconnect()
                stateContext.getState().stop()
            }
            else -> {
                Jivo.e("Unhandled disconnected reason $reason in connected state, try reconnect")
                stateContext.getState().reconnect(false)
            }
        }

    }

    override fun restart() {
        stateContext.changeState(DisconnectedState::class.java)
        connectionStateRepository.setState(ConnectionState.Disconnected(0))
        service.releaseConnectionKeeper()
        service.unsubscribeFromTransmitter()
        stateContext.getState().reconnect(true)
    }
}