package com.jivosite.sdk.socket.states.items

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.model.repository.connection.ConnectionState
import com.jivosite.sdk.model.repository.connection.ConnectionStateRepository
import com.jivosite.sdk.model.repository.contacts.ContactFormRepository
import com.jivosite.sdk.socket.JivoWebSocketService
import com.jivosite.sdk.socket.states.DisconnectReason
import com.jivosite.sdk.socket.states.ServiceState
import com.jivosite.sdk.socket.states.ServiceStateContext
import com.jivosite.sdk.socket.support.ReconnectStrategy
import com.jivosite.sdk.support.usecase.SubscribePushTokenUseCase
import javax.inject.Inject

/**
 * Created on 06.09.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class ConnectingState @Inject constructor(
    stateContext: ServiceStateContext,
    private val service: JivoWebSocketService,
    private val reconnectStrategy: ReconnectStrategy,
    private val connectionStateRepository: ConnectionStateRepository,
    private val agentRepository: AgentRepository,
    private val contactFormRepository: ContactFormRepository,
    private val subscribePushTokenUseCase: SubscribePushTokenUseCase
) : ServiceState(stateContext) {

    override fun load() {
        logImpossibleAction("load")
    }

    override fun start() {
        stateContext.changeState(ConnectingState::class.java)
        connectionStateRepository.setState(ConnectionState.Connecting)
        service.connect()
    }

    override fun reconnect(force: Boolean) {
        logImpossibleAction("reconnect")
    }

    override fun setConnected() {
        stateContext.changeState(ConnectedState::class.java)
        connectionStateRepository.setState(ConnectionState.Connected)
        agentRepository.onConnectionStateChanged()
        contactFormRepository.prepareToSendContactInfo()
        contactFormRepository.sendCustomData()
        service.keepConnection()
        //reconnectStrategy.reset()
        service.subscribeToTransmitter()
        subscribePushTokenUseCase.execute()
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
        stateContext.changeState(DisconnectedState::class.java)
        connectionStateRepository.setState(ConnectionState.Disconnected(0))

        val force = when (reason) {
            DisconnectReason.BlackListed, DisconnectReason.Sanctioned -> {
                stateContext.getState().stop()
                false
            }
            else -> {
                Jivo.e("Unhandled disconnected reason $reason in connecting state, try reconnect")
                false
            }
        }
        stateContext.getState().reconnect(force)
    }

    override fun restart() {
        logImpossibleAction("restart")
    }

    override fun error(reason: String) {
        logImpossibleAction("error")
    }
}