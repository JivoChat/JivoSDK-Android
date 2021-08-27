package com.jivosite.sdk.socket

import android.app.ActivityManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import com.jivosite.sdk.BuildConfig
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.logger.Logger
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.socket.endpoint.SocketEndpointProvider
import com.jivosite.sdk.socket.handler.SocketMessageHandler
import com.jivosite.sdk.socket.keeper.ConnectionKeeper
import com.jivosite.sdk.socket.states.DisconnectReason
import com.jivosite.sdk.socket.states.ServiceState
import com.jivosite.sdk.socket.states.ServiceStateContext
import com.jivosite.sdk.socket.states.ServiceStateFactory
import com.jivosite.sdk.socket.states.items.*
import com.jivosite.sdk.socket.transmitter.Transmitter
import com.jivosite.sdk.socket.transmitter.TransmitterSubscriber
import com.neovisionaries.ws.client.*
import java.util.*
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created on 05.09.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class JivoWebSocketService : Service(), ServiceStateContext, TransmitterSubscriber {

    companion object {
        private const val ACTION_START = "com.jivosite.sdk.socket.JivoWebSocketService.ACTION_START"
        private const val ACTION_STOP = "com.jivosite.sdk.socket.JivoWebSocketService.ACTION_STOP"
        private const val ACTION_RESTART = "com.jivosite.sdk.socket.JivoWebSocketService.ACTION_RESTART"
        private const val ACTION_SET_CLIENT_INFO = "com.jivosite.sdk.socket.JivoWebSocketService.ACTION_SET_CLIENT_INFO"

        const val REASON_STOPPED = 4000
        const val REASON_TIMEOUT = 4001

        fun start(appContext: Context) {
            val intent = Intent(appContext, JivoWebSocketService::class.java).apply {
                action = ACTION_START
            }
            val activityManager = appContext.getSystemService(ACTIVITY_SERVICE) as ActivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                if (!activityManager.isBackgroundRestricted) {
                    appContext.startService(intent)
                } else {
                    Jivo.e("Can not start jivo sdk service from background")
                }
            } else {
                appContext.startService(intent)
            }
        }

        fun stop(appContext: Context) {
            val intent = Intent(appContext, JivoWebSocketService::class.java).apply {
                action = ACTION_STOP
            }
            appContext.startService(intent)
        }

        fun restart(appContext: Context) {
            val intent = Intent(appContext, JivoWebSocketService::class.java).apply {
                action = ACTION_RESTART
            }
            appContext.startService(intent)
        }

        fun setClientInfo(appContext: Context, args: Bundle) {
            val intent = Intent(appContext, JivoWebSocketService::class.java).apply {
                action = ACTION_SET_CLIENT_INFO
                putExtras(args)
            }
            appContext.startService(intent)
        }
    }

    private val binder = JivoWebSocketServiceBinder()

    @Inject
    lateinit var socketEndpointProvider: Provider<SocketEndpointProvider>

    @Inject
    lateinit var socketMessageHandler: SocketMessageHandler

    @Inject
    lateinit var serviceStateFactory: ServiceStateFactory

    @Inject
    lateinit var messageLogger: Logger

    @Inject
    lateinit var messageTransmitter: Transmitter

    private lateinit var socketState: ServiceState
    private var webSocket: WebSocket? = null
    private var connectionKeeper: ConnectionKeeper? = null

    private val webSocketListener: WebSocketListener = object : WebSocketAdapter() {

        override fun onConnected(websocket: WebSocket?, headers: MutableMap<String, MutableList<String>>?) {
            messageLogger.logConnected()
            getState().setConnected()
        }

        override fun onTextMessage(websocket: WebSocket?, text: String?) {
            val msg = text ?: return
            connectionKeeper?.resetTimeout()
            if (msg.isBlank()) {
                messageLogger.logPong(msg)
                Jivo.d("PONG")
            } else {
                messageLogger.logReceivedMessage(msg)
                socketMessageHandler.handle(msg)
            }
        }

        override fun onDisconnected(
            websocket: WebSocket?,
            serverCloseFrame: WebSocketFrame?,
            clientCloseFrame: WebSocketFrame?,
            closedByServer: Boolean
        ) {
            messageLogger.logDisconnected(clientCloseFrame?.closeCode ?: 0, clientCloseFrame?.closeReason ?: "")

            Jivo.i("Socket disconnected, code=${clientCloseFrame?.closeCode}, reason=${clientCloseFrame?.closeReason}")

            webSocket?.removeListener(this)
            when (val state = getState()) {
                is ConnectedState -> {
                    val reason = when (clientCloseFrame?.closeCode) {
                        1013 -> DisconnectReason.DisconnectedByServer(1013, clientCloseFrame.closeReason)
                        else -> DisconnectReason.Unknown
                    }
                    state.setDisconnected(reason)
                }
                is DisconnectedState -> state.setDisconnected(DisconnectReason.Unknown)
                is StoppedState -> state.setDisconnected(DisconnectReason.Stopped)
            }
        }

        override fun onError(websocket: WebSocket?, cause: WebSocketException?) {
            super.onError(websocket, cause)
            Jivo.e("onError, $cause")
            socketErrorHandler(cause) {
                default {
                    cause?.run { messageLogger.logError(this) }
                    getState().setDisconnected(DisconnectReason.Unknown)
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Jivo.getServiceComponent(this).inject(this)
        Jivo.i("Service has been created")
        changeState(InitialState::class.java)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action ?: return START_NOT_STICKY
        when (action) {
            ACTION_START -> {
                Jivo.i("Received start command")
                getState().start()
            }
            ACTION_STOP -> {
                Jivo.i("Received stop command")
                getState().stop()
            }
            ACTION_RESTART -> {
                Jivo.i("Received restart command")
                getState().restart()
            }

            ACTION_SET_CLIENT_INFO -> {
                Jivo.i("Received set client info command")
                val clientInfo = mapOf(
                    "atom/user.name" to intent.getStringExtra("name"),
                    "atom/user.email" to intent.getStringExtra("email"),
                    "atom/user.phone" to intent.getStringExtra("phone"),
                    "atom/user.desc" to intent.getStringExtra("description")
                )

                val clientId = intent.getStringExtra("clientId")

                for ((key, value) in clientInfo) {
                    if (!value.isNullOrBlank() && !clientId.isNullOrBlank()) {
                        getState().send(SocketMessage.clientInfo(key, value, clientId))
                    }
                }
            }

            else -> Jivo.w("Unknown command $action")
        }
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onDestroy() {
        super.onDestroy()
        Jivo.clearServiceComponent()
        Jivo.i("Service has been destroyed")
    }

    override fun getState(): ServiceState = socketState

    override fun changeState(state: Class<out ServiceState>): ServiceState {
        val newState = serviceStateFactory.create(state)
        Jivo.i("Change state to $newState")
        socketState = newState
        return socketState
    }

    internal fun connect() {
        webSocket?.run {
            removeListener(webSocketListener)
        }
        val endpoint = socketEndpointProvider.get().getEndpoint()
        webSocket = WebSocketFactory()
            .createSocket(endpoint, BuildConfig.CONNECTION_TIMEOUT)
            .apply {
                Jivo.i("Try to connect to endpoint: $endpoint")
                addHeader("User-Agent", "sdk/${BuildConfig.VERSION_NAME} (Android ${Build.VERSION.RELEASE})")
                addListener(webSocketListener)
                connectAsynchronously()
                messageLogger.logConnecting()
            }
    }

    internal fun send(msg: String) {
        messageLogger.logSentMessage(msg)
        webSocket?.sendText(msg)
        connectionKeeper?.resetPing()
    }

    internal fun disconnect() {
        webSocket?.disconnect(REASON_STOPPED, "Disconnect by sdk")
    }

    internal fun keepConnection() {
        Jivo.i("Start keep connection alive")
        releaseConnectionKeeper()
        webSocket?.let {
            connectionKeeper = ConnectionKeeper(it, BuildConfig.PING_INTERVAL, BuildConfig.PONG_INTERVAL, messageLogger).apply {
                resetPing()
            }
        }
    }

    internal fun releaseConnectionKeeper() {
        connectionKeeper?.run {
            Jivo.i("Release connection keeper")
            release()
        }
        connectionKeeper = null
    }

    internal fun subscribeToTransmitter() {
        Jivo.d("Subscribe to message transmitter")
        messageTransmitter.addSubscriber(this)
    }

    override fun sendMessage(message: SocketMessage) {
        Jivo.d("Send message through transmitter - $message")
        getState().send(message)
    }

    override fun sendMessage(message: String) {
        Jivo.d("Send message through transmitter - $message")
        getState().send(message)
    }

    internal fun unsubscribeFromTransmitter() {
        Jivo.d("Unsubscribe from message transmitter")
        messageTransmitter.removeSubscriber(this)
    }

    inner class JivoWebSocketServiceBinder : Binder() {

        fun sendMessage(message: SocketMessage) {
            this@JivoWebSocketService.getState().send(message)
        }

        fun retry() {
            this@JivoWebSocketService.getState().reconnect(true)
        }

        fun restart() {
            this@JivoWebSocketService.getState().setDisconnected(DisconnectReason.ChangeInstance)
        }
    }
}