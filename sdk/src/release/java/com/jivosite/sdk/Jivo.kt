package com.jivosite.sdk

import android.content.Context
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
import com.jivosite.sdk.model.pojo.CustomData
import com.jivosite.sdk.model.repository.history.NewMessageListener
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.socket.JivoWebSocketService
import com.jivosite.sdk.support.builders.ContactInfo
import com.jivosite.sdk.support.builders.Config
import com.jivosite.sdk.support.ext.verifyHostName
import com.jivosite.sdk.ui.chat.NotificationPermissionListener
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
    private val notificationPermissionListener: ArrayList<WeakReference<NotificationPermissionListener>> = ArrayList()

    private var lifecycleObserver: JivoLifecycleObserver? = null
    private lateinit var sdkContext: SdkContext
    private lateinit var storage: SharedStorage

    private var config: Config = Config.Builder().build()

    private var loggingEnabled = false

    @JvmStatic
    fun init(appContext: Context, widgetId: String, host: String = "") {
        jivoSdkComponent = DaggerJivoSdkComponent.builder()
            .sdkModule(SdkModule(appContext, widgetId))
            .build()
        sdkContext = jivoSdkComponent.sdkContext()
        storage = jivoSdkComponent.storage()

        if (host.verifyHostName()) {
            storage.host = host
        }
    }

    @JvmStatic
    fun changeChannelId(widgetId: String) {
        if (Jivo::jivoSdkComponent.isInitialized && widgetId != storage.widgetId) {
            jivoSdkComponent.clearUseCaseProvider().get().execute()
            unsubscribeFromPush()
            storage.widgetId = widgetId
            JivoWebSocketService.loadConfig(sdkContext.appContext)
        }
    }

    @JvmStatic
    fun setContactInfo(contactInfo: ContactInfo) {
        if (Jivo::jivoSdkComponent.isInitialized) {
            jivoSdkComponent.contactFormRepository().prepareToSendContactInfo(contactInfo)
        }
    }

    @JvmStatic
    fun setCustomData(customDataFields: List<CustomData>) {
        if (Jivo::jivoSdkComponent.isInitialized) {
            jivoSdkComponent.contactFormRepository().sendCustomData(customDataFields)
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
        if (Jivo::jivoSdkComponent.isInitialized) {
            if (storage.pushToken != token) {
                storage.pushToken = token
                storage.hasSentPushToken = false
                jivoSdkComponent.updatePushTokenUseCaseProvider().get().execute()
            }
        }
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
    fun addNotificationPermissionListener(l: NotificationPermissionListener) {
        notificationPermissionListener.add(WeakReference(l))
    }

    @JvmStatic
    fun setUserToken(userToken: String) {
        if (Jivo::jivoSdkComponent.isInitialized) {
            if (userToken.isNotBlank() && userToken != storage.userToken) {
                jivoSdkComponent.clearUseCaseProvider().get().execute()
                storage.userToken = userToken
                JivoWebSocketService.restart(sdkContext.appContext)
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
        if (Jivo::jivoSdkComponent.isInitialized) {
            unsubscribeFromPush()
            jivoSdkComponent.clearUseCaseProvider().get().execute()
            JivoWebSocketService.stop(sdkContext.appContext)
        }
    }

    @JvmStatic
    fun unsubscribeFromPush() {
        if (Jivo::jivoSdkComponent.isInitialized) {
            storage.pushToken = ""
            jivoSdkComponent.updatePushTokenUseCaseProvider().get().execute()
        }
    }

    internal fun startSession() {
        if (lifecycleObserver == null) {
            lifecycleObserver = JivoLifecycleObserver(sdkContext, storage)

            lifecycleObserver?.let {
                ProcessLifecycleOwner.get().lifecycle.addObserver(it.apply {
                    onForeground()
                })
            }
        } else {
            lifecycleObserver?.onForeground()
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

    internal fun isPermissionGranted(isGranted: Boolean): Boolean {
        val filteredList = notificationPermissionListener.filter { it.get() != null }
        notificationPermissionListener.clear()

        if (filteredList.isEmpty()) return false

        notificationPermissionListener.addAll(filteredList)
        notificationPermissionListener.forEach {
            it.get()?.onNotificationPermissionGranted(isGranted)
        }
        return true
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