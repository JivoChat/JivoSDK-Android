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
import javax.inject.Inject

/**
 * Created on 06.09.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class DisconnectedState @Inject constructor(
    stateContext: ServiceStateContext,
    private val service: JivoWebSocketService,
    private val reconnectStrategy: ReconnectStrategy,
    private val connectionStateRepository: ConnectionStateRepository
) : ServiceState(stateContext) {

    private val handler = Handler(Looper.getMainLooper())
    private val reconnectCallback = Runnable {
        stateContext.changeState(ConnectingState::class.java)
        connectionStateRepository.setState(ConnectionState.Connecting)
        service.connect()
    }

    override fun start() {
        logImpossibleAction("start")
    }

    override fun reconnect(force: Boolean) {
        handler.removeCallbacks(reconnectCallback)
        if (force) {
            handler.post(reconnectCallback)
        } else {
            val timeout = reconnectStrategy.nextTime()
            Jivo.i("Wait for $timeout ms and reconnect")
            connectionStateRepository.setState(ConnectionState.Disconnected(System.currentTimeMillis() + timeout))
            handler.postDelayed(reconnectCallback, timeout)
        }
    }

    override fun setConnected() {
        logImpossibleAction("setConnected")
    }

    override fun send(message: SocketMessage) {
        logImpossibleAction("send(socketMessage)")
    }

    override fun send(message: String) {
        logImpossibleAction("send(String)")
    }

    override fun stop() {
        stateContext.changeState(StoppedState::class.java)
        connectionStateRepository.setState(ConnectionState.Stopped)
        handler.removeCallbacks(reconnectCallback)

        Jivo.i("Service stopped from disconnected state")
        service.stopSelf()
    }

    override fun setDisconnected(reason: DisconnectReason) {
        Jivo.w("Call set disconnected on disconnected state, just ignore action")
    }

    override fun restart() {
        logImpossibleAction("restart")
    }
}