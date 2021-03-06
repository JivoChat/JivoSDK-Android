package com.jivosite.sdk

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.firebase.messaging.RemoteMessage
import com.jivosite.sdk.di.DaggerJivoSdkComponent
import com.jivosite.sdk.di.JivoSdkComponent
import com.jivosite.sdk.di.modules.SdkModule
import com.jivosite.sdk.di.service.WebSocketServiceComponent
import com.jivosite.sdk.di.service.modules.SocketMessageHandlerModule
import com.jivosite.sdk.di.service.modules.StateModule
import com.jivosite.sdk.di.service.modules.WebSocketServiceModule
import com.jivosite.sdk.di.ui.chat.JivoChatComponent
import com.jivosite.sdk.di.ui.chat.JivoChatFragmentModule
import com.jivosite.sdk.lifecycle.JivoLifecycleObserver
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.repository.history.NewMessageListener
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.socket.JivoWebSocketService
import com.jivosite.sdk.support.builders.ClientInfo
import com.jivosite.sdk.support.builders.Config
import com.jivosite.sdk.support.ext.toMD5
import timber.log.Timber
import java.lang.ref.WeakReference

/**
 * Created on 02.09.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
object Jivo {

    private const val TAG = "JivoSDK"

    internal lateinit var jivoSdkComponent: JivoSdkComponent
    private var serviceComponent: WebSocketServiceComponent? = null
    private var chatComponent: JivoChatComponent? = null

    private val newMessageListeners: ArrayList<WeakReference<NewMessageListener>> = ArrayList()

    private lateinit var lifecycleObserver: JivoLifecycleObserver
    private lateinit var sdkContext: SdkContext
    private lateinit var storage: SharedStorage

    private var config: Config = Config.Builder().build()

    private var loggingEnabled = false

    private val handler = Handler(Looper.getMainLooper())
    private val updatePushTokenCallback = object : Runnable {
        lateinit var token: String
        override fun run() {
            if (Jivo::jivoSdkComponent.isInitialized) {
                val useCaseProvider = jivoSdkComponent.updatePushTokenUseCaseProvider()
                useCaseProvider.get().execute(token)
            }
        }
    }

    @JvmStatic
    fun init(appContext: Context, widgetId: String, host: String = "") {
        jivoSdkComponent = DaggerJivoSdkComponent.builder()
            .sdkModule(SdkModule(appContext, widgetId))
            .build()
        sdkContext = jivoSdkComponent.sdkContext()

        storage = jivoSdkComponent.storage()

        if (host.isNotBlank()) {
            storage.host = host
        }

        val sdkConfigUseCaseProvider = jivoSdkComponent.sdkConfigUseCaseProvider()

        lifecycleObserver = JivoLifecycleObserver(sdkContext, storage, sdkConfigUseCaseProvider.get())
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver)
    }

    @JvmStatic
    fun setClientInfo(clientInfo: ClientInfo) {
        if (Jivo::jivoSdkComponent.isInitialized) {
            val args = bundleOf(
                "name" to clientInfo.name,
                "email" to clientInfo.email,
                "phone" to clientInfo.phone,
                "description" to clientInfo.description,
                "clientId" to jivoSdkComponent.storage().clientId
            )
            JivoWebSocketService.setClientInfo(sdkContext.appContext, args)
        }
    }

    @JvmStatic
    fun handleRemoteMessage(message: RemoteMessage): Boolean {
        return if (Jivo::jivoSdkComponent.isInitialized) {
            val handler = jivoSdkComponent.remoteMessageHandler()
            handler.handleRemoteMessage(message)
        } else {
            false
        }
    }

    @JvmStatic
    fun updatePushToken(token: String) {
        handler.removeCallbacks(updatePushTokenCallback)
        handler.postDelayed(updatePushTokenCallback.also { it.token = token }, 1000)
    }

    @JvmStatic
    fun setConfig(config: Config) {
        this.config = config
    }

    @JvmStatic
    fun enableLogging() {
        loggingEnabled = true
    }

    @JvmStatic
    fun disableInAppNotification(isDisabled: Boolean) {
        storage.inAppNotificationEnabled = !isDisabled
    }

    @JvmStatic
    fun addNewMessageListener(l: NewMessageListener) {
        if (Jivo::jivoSdkComponent.isInitialized) {
            l.onNewMessage(jivoSdkComponent.historyRepository().state.hasUnread)
        }
        newMessageListeners.add(WeakReference(l))
    }

    @JvmStatic
    fun setUserToken(userToken: String) {
        if (Jivo::jivoSdkComponent.isInitialized) {
            val md5 = userToken.toMD5()
            if (md5 != storage.userTokenHash) {
                jivoSdkComponent.clearUseCaseProvider().get().execute()
                storage.userTokenHash = md5

                val args = bundleOf("userToken" to userToken)
                JivoWebSocketService.restart(sdkContext.appContext, args)
            }
        }
    }

    fun turnOn() {
        jivoSdkComponent.storage().let {
            if (!it.startOnInitialization) {
                it.startOnInitialization = true
                JivoWebSocketService.start(sdkContext.appContext)
            }
        }
    }

    fun turnOff() {
        jivoSdkComponent.storage().let {
            if (it.startOnInitialization) {
                it.startOnInitialization = false
                JivoWebSocketService.stop(sdkContext.appContext)
            }
        }
    }

    @JvmStatic
    fun clear() {
        handler.removeCallbacks(updatePushTokenCallback)
        if (Jivo::jivoSdkComponent.isInitialized) {
            unsubscribeFromPush()
            jivoSdkComponent.clearUseCaseProvider().get().execute()
            JivoWebSocketService.restart(sdkContext.appContext)
        }
    }

    fun subscribeToPush() {
        if (Jivo::jivoSdkComponent.isInitialized) {
            val useCaseProvider = jivoSdkComponent.updatePushTokenUseCaseProvider()
            useCaseProvider.get().execute()
        }
    }

    fun unsubscribeFromPush() {
        if (Jivo::jivoSdkComponent.isInitialized) {
            val useCaseProvider = jivoSdkComponent.updatePushTokenUseCaseProvider()
            useCaseProvider.get().execute("")
        }
    }

    internal fun onNewMessage(hasNewMessage: Boolean) {
        val filteredList = newMessageListeners.filter { it.get() != null }
        newMessageListeners.clear()
        newMessageListeners.addAll(filteredList)

        newMessageListeners.forEach {
            it.get()?.onNewMessage(hasNewMessage)
        }
    }

    internal fun getConfig(): Config {
        return config
    }

    internal fun getServiceComponent(service: JivoWebSocketService): WebSocketServiceComponent {
        return serviceComponent ?: jivoSdkComponent.serviceComponent(
            WebSocketServiceModule(service),
            StateModule(),
            SocketMessageHandlerModule()
        ).also {
            serviceComponent = it
        }
    }

    internal fun getChatComponent(fragment: Fragment): JivoChatComponent {
        return chatComponent ?: jivoSdkComponent.chatComponent(JivoChatFragmentModule(fragment))
            .also { chatComponent = it }
    }

    internal fun clearServiceComponent() {
        serviceComponent = null
    }

    internal fun clearChatComponent() {
        chatComponent = null
    }

    internal fun restart() {
        sdkContext.pendingIntent.clear()
        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleObserver.onForeground()
        }, 10)
    }

    internal fun d(msg: String) {
        if (loggingEnabled) {
            Timber.tag(TAG).d(msg)
        }
    }

    internal fun e(msg: String) {
        if (loggingEnabled) {
            Timber.tag(TAG).e(msg)
        }
    }

    internal fun e(e: Throwable, msg: String) {
        if (loggingEnabled) {
            Timber.tag(TAG).e(e, msg)
        }
    }

    internal fun i(msg: String) {
        if (loggingEnabled) {
            Timber.tag(TAG).i(msg)
        }
    }

    internal fun w(msg: String) {
        if (loggingEnabled) {
            Timber.tag(TAG).w(msg)
        }
    }
}