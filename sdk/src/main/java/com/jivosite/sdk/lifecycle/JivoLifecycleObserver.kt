package com.jivosite.sdk.lifecycle

import androidx.lifecycle.*
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.socket.JivoWebSocketService
import com.jivosite.sdk.socket.JivoWebSocketService.Companion.isStartedService
import com.jivosite.sdk.support.usecase.RecoveryClientIdUseCase
import com.jivosite.sdk.support.usecase.HistoryUseCase
import com.jivosite.sdk.support.utils.after
import com.jivosite.sdk.support.utils.convertTimeMillisToDateFormat
import com.jivosite.sdk.support.utils.hasServiceRunning

/**
 * Created on 19.11.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class JivoLifecycleObserver(
    private val sdkContext: SdkContext,
    private val storage: SharedStorage,
    private val historyUseCase: HistoryUseCase,
    private val clientIdRecoveryUseCase: RecoveryClientIdUseCase,
) : DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        if (sdkContext.appContext.hasServiceRunning(JivoWebSocketService::class.java)) {
            JivoWebSocketService.forcedStop(sdkContext.appContext)
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        clientIdRecoveryUseCase.execute()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        historyUseCase.execute()
    }

    fun onBackground() {
        when {
            storage.widgetId.isBlank() -> {
                Jivo.e("WidgetId is empty, service is turned off")
            }

            storage.blacklistedTime.after() -> {
                Jivo.i("Application moved to background. Blacklisted until ${convertTimeMillisToDateFormat(storage.blacklistedTime)}, service is turned off")
            }

            storage.sanctionedTime.after() -> {
                Jivo.i("Application moved to background. Sanctioned until ${convertTimeMillisToDateFormat(storage.sanctionedTime)}, service is turned off")
            }

            storage.startOnInitialization && isStartedService -> {
                JivoWebSocketService.stop(sdkContext.appContext)
            }
        }
        Jivo.i("SDK moved to background, is started service - $isStartedService")
    }

    fun onForeground() {
        when {
            storage.widgetId.isBlank() -> {
                Jivo.e("WidgetId is empty, service is turned off")
            }

            storage.blacklistedTime.after() -> {
                Jivo.i("Blacklisted until ${convertTimeMillisToDateFormat(storage.blacklistedTime)}, service is turned off")
            }

            storage.sanctionedTime.after() -> {
                Jivo.i("Sanctioned until ${convertTimeMillisToDateFormat(storage.sanctionedTime)}, service is turned off")
            }

            storage.startOnInitialization && !isStartedService -> {
                JivoWebSocketService.loadConfig(sdkContext.appContext)
                Jivo.i("SDK moved to foreground, load config")
            }

            else -> {
                Jivo.i("SDK moved to foreground, service is turned off")
            }
        }
    }

    fun startNewSession() {
        if (isStartedService) {
            Jivo.d("New session is started, load config")
            JivoWebSocketService.connect(sdkContext.appContext)
        }
    }

    fun stopSession() {
        if (isStartedService) {
            Jivo.i("Session is stopped, service is turned off")
            JivoWebSocketService.stop(sdkContext.appContext)
        }
    }
}