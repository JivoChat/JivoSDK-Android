package com.jivosite.sdk

import android.content.Context
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ProcessLifecycleOwner
import com.jivosite.sdk.di.DaggerJivoSdkComponent
import com.jivosite.sdk.di.JivoSdkComponent
import com.jivosite.sdk.di.modules.SdkModule
import com.jivosite.sdk.di.service.PushServiceComponent
import com.jivosite.sdk.di.service.WebSocketServiceComponent
import com.jivosite.sdk.di.service.modules.PushServiceModule
import com.jivosite.sdk.di.service.modules.SocketMessageHandlerModule
import com.jivosite.sdk.di.service.modules.StateModule
import com.jivosite.sdk.di.service.modules.WebSocketServiceModule
import com.jivosite.sdk.di.ui.chat.JivoChatComponent
import com.jivosite.sdk.di.ui.chat.JivoChatFragmentModule
import com.jivosite.sdk.di.ui.logs.JivoLogsComponent
import com.jivosite.sdk.di.ui.logs.JivoLogsFragmentModule
import com.jivosite.sdk.di.ui.settings.JivoSettingsComponent
import com.jivosite.sdk.di.ui.settings.JivoSettingsFragmentModule
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.push.JivoFirebaseMessagingService
import com.jivosite.sdk.socket.JivoWebSocketService
import com.jivosite.sdk.ui.logs.JivoLogsFragment
import com.jivosite.sdk.ui.settings.JivoSettingsFragment
import timber.log.Timber

/**
 * Created on 02.09.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
object Jivo {

    private const val TAG = "JivoSDK"

    internal lateinit var jivoSdkComponent: JivoSdkComponent
    private var serviceComponent: WebSocketServiceComponent? = null
    private var chatComponent: JivoChatComponent? = null
    private var logsComponent: JivoLogsComponent? = null
    private var settingsComponent: JivoSettingsComponent? = null
    private var pushComponent: PushServiceComponent? = null

    private lateinit var lifecycleObserver: JivoLifecycleObserver
    private lateinit var sdkContext: SdkContext

    @JvmStatic
    fun init(appContext: Context, siteId: Long, widgetId: String, host: String = "", port: String = "") {
        jivoSdkComponent = DaggerJivoSdkComponent.builder()
                .sdkModule(SdkModule(appContext, siteId, widgetId))
                .build()
        sdkContext = jivoSdkComponent.sdkContext()

        val storage = jivoSdkComponent.storage()

        if (host.isNotBlank() && port.isNotBlank()) {
            storage.host = host
            storage.port = port
        }

        lifecycleObserver = JivoLifecycleObserver(sdkContext, storage)
        ProcessLifecycleOwner.get().lifecycle.addObserver(lifecycleObserver)
    }

    /**
     * Передача информации о клиенте
     *
     * @param name Имя пользователя, используется как обращение в сообщениях.
     * @param email Адрес электронной почты, для отправки сообщений.
     * @param phone Номер телефона для звонков
     * @param description Описание, как комментарий, можно почтовый адрес, должность и пр.
     *
     */
    fun setClientInfo(name: String = "", email: String = "", phone: String = "", description: String = "") {
        val args = bundleOf(
                "name" to name,
                "email" to email,
                "phone" to phone,
                "description" to description,
                "clientId" to jivoSdkComponent.storage().clientId
        )
        JivoWebSocketService.setClientInfo(sdkContext.appContext, args)
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

    fun clear() {
        JivoWebSocketService.restart(sdkContext.appContext)
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

    internal fun getPushServiceComponent(service: JivoFirebaseMessagingService): PushServiceComponent {
        return pushComponent ?: jivoSdkComponent.pushComponent(PushServiceModule(service))
                .also { pushComponent = it }
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

    internal fun clearPushServiceComponent() {
        pushComponent = null
    }

    internal fun d(msg: String) {
        Timber.tag(TAG).d(msg)
    }

    internal fun e(msg: String) {
        Timber.tag(TAG).e(msg)
    }

    internal fun e(e: Throwable, msg: String) {
        Timber.tag(TAG).e(e, msg)
    }

    internal fun i(msg: String) {
        Timber.tag(TAG).i(msg)
    }

    internal fun w(msg: String) {
        Timber.tag(TAG).w(msg)
    }
}