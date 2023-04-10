package com.jivosite.sdk.socket.states.items

import android.os.Handler
import android.os.Looper
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.connection.ConnectionState
import com.jivosite.sdk.model.repository.connection.ConnectionStateRepository
import com.jivosite.sdk.socket.JivoWebSocketService
import com.jivosite.sdk.socket.states.DisconnectReason
import com.jivosite.sdk.socket.states.ServiceState
import com.jivosite.sdk.socket.states.ServiceStateContext
import com.jivosite.sdk.socket.support.ReconnectStrategy
import com.jivosite.sdk.support.usecase.SdkConfigUseCase
import javax.inject.Inject

/**
 * Created on 05.04.2023.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class ErrorState @Inject constructor(
    stateContext: ServiceStateContext,
    private val service: JivoWebSocketService,
    private val sdkConfigUseCase: SdkConfigUseCase,
    private val reconnectStrategy: ReconnectStrategy,
    private val connectionStateRepository: ConnectionStateRepository,
) : ServiceState(stateContext) {

    private val handler = Handler(Looper.getMainLooper())
    private val reconnectCallback = Runnable {
        stateContext.changeState(LoadConfigState::class.java)
        connectionStateRepository.setState(ConnectionState.LoadConfig)
        sdkConfigUseCase.execute()
    }

    override fun start() {
        logImpossibleAction("start")
    }

    override fun load() {
        logImpossibleAction(" load")
    }

    override fun reconnect(force: Boolean) {
        handler.removeCallbacks(reconnectCallback)
        if (force) {
            handler.post(reconnectCallback)
        } else {
            val timeout = reconnectStrategy.nextTime()
            Jivo.i("Wait for $timeout ms and reconnect")
            connectionStateRepository.setState(ConnectionState.Error(System.currentTimeMillis() + timeout))
            handler.postDelayed(reconnectCallback, timeout)
        }
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
        stateContext.getState().reconnect(false)
    }
}