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
import com.jivosite.sdk.di.ui.logs.JivoLogsComponent
import com.jivosite.sdk.di.ui.logs.JivoLogsFragmentModule
import com.jivosite.sdk.di.ui.settings.JivoSettingsComponent
import com.jivosite.sdk.di.ui.settings.JivoSettingsFragmentModule
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
import com.jivosite.sdk.ui.logs.JivoLogsFragment
import com.jivosite.sdk.ui.settings.JivoSettingsFragment
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
    private var logsComponent: JivoLogsComponent? = null
    private var settingsComponent: JivoSettingsComponent? = null

    private val newMessageListeners: ArrayList<WeakReference<NewMessageListener>> = ArrayList()
    private val notificationPermissionListener: ArrayList<WeakReference<NotificationPermissionListener>> = ArrayList()

    private lateinit var lifecycleObserver: JivoLifecycleObserver
    private lateinit var sdkContext: SdkContext
    private lateinit var storage: SharedStorage

    private var config: Config = Config.Builder().build()

    private var loggingEnabled = false

    @JvmStatic
    fun init(appContext: Context) {
        jivoSdkComponent = DaggerJivoSdkComponent.builder()
            .sdkModule(SdkModule(appContext))
            .build()
        sdkContext = jivoSdkComponent.sdkContext()
        storage = jivoSdkComponent.storage()

        lifecycleObserver = JivoLifecycleObserver(
            sdkContext,
            storage,
            jivoSdkComponent.historyUseCase().get()
        ).apply {
            ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        }
    }

    @JvmStatic
    fun setData(widgetId: String, userToken: String = "", host: String = "") {
        if (Jivo::jivoSdkComponent.isInitialized) {
            if (widgetId.isNotBlank() && widgetId != storage.widgetId || userToken.isNotBlank() && userToken != storage.userToken) {
                if (storage.clientId.isNotBlank()) {
                    jivoSdkComponent.unsubscribePushTokenUseCaseProvider().get().onSuccess {
                        jivoSdkComponent.clearUseCaseProvider().get().execute()
                        lifecycleObserver.stopSession()
                        storage.userToken = userToken
                        storage.widgetId = widgetId
                    }.execute()
                } else {
                    storage.userToken = userToken
                    storage.widgetId = widgetId
                }
            }

            if (host.verifyHostName()) {
                storage.host = host
            }
        }
    }

    @JvmStatic
    fun setContactInfo(contactInfo: ContactInfo) {
        if (Jivo::jivoSdkComponent.isInitialized) {
            jivoSdkComponent.contactFormRepository().prepareToSendContactInfo(contactInfo)
        } else {
            e("Call setContactInfo(), JivoSdkComponent hasn't isInitialized")
        }
        i("Call setContactInfo($contactInfo)")
    }

    @JvmStatic
    fun setCustomData(customDataFields: List<CustomData>) {
        if (Jivo::jivoSdkComponent.isInitialized) {
            jivoSdkComponent.contactFormRepository().prepareToSendCustomData(customDataFields)
        } else {
            e("Call setContactInfo(), JivoSdkComponent hasn't isInitialized")
        }
        i("Call setCustomData($customDataFields)")
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
                storage.hasSentPushToken = false
                storage.pushToken = token
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
            jivoSdkComponent.unsubscribePushTokenUseCaseProvider().get().onSuccess {
                jivoSdkComponent.clearUseCaseProvider().get().execute()
                lifecycleObserver.stopSession()
            }.execute()
        }
    }

    @JvmStatic
    fun unsubscribeFromPush() {
        if (Jivo::jivoSdkComponent.isInitialized) {
            jivoSdkComponent.unsubscribePushTokenUseCaseProvider().get().execute()
        }
    }

    internal fun startSession() {
        lifecycleObserver.onForeground()
    }

    internal fun stopSession() {
        lifecycleObserver.stopSession()
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

    internal fun getLogsComponent(fragment: JivoLogsFragment): JivoLogsComponent {
        return logsComponent ?: jivoSdkComponent.logsComponent(JivoLogsFragmentModule(fragment))
            .also { logsComponent = it }
    }

    internal fun getSettingsComponent(fragment: JivoSettingsFragment): JivoSettingsComponent {
        return settingsComponent
            ?: jivoSdkComponent.settingsComponent(JivoSettingsFragmentModule(fragment))
                .also { settingsComponent = it }
    }

    internal fun clearServiceComponent() {
        serviceComponent = null
    }

    internal fun clearChatComponent() {
        chatComponent = null
    }

    internal fun clearLogsComponent() {
        logsComponent = null
    }

    internal fun clearSettingsComponent() {
        settingsComponent = null
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