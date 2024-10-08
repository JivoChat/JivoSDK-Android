package com.jivosite.sdk.lifecycle

import androidx.lifecycle.*
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.socket.JivoWebSocketService
import com.jivosite.sdk.socket.JivoWebSocketService.Companion.isStartedService
import com.jivosite.sdk.support.usecase.HistoryUseCase
import com.jivosite.sdk.support.utils.after
import com.jivosite.sdk.support.utils.convertTimeMillisToDateFormat

/**
 * Created on 19.11.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class JivoLifecycleObserver(
    private val sdkContext: SdkContext,
    private val storage: SharedStorage,
    private val historyUseCase: HistoryUseCase,
) : DefaultLifecycleObserver {

    override fun onResume(owner: LifecycleOwner) {
        historyUseCase.execute()
    }

    override fun onStop(owner: LifecycleOwner) {
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
                Jivo.i("Application moved to background, stop service")
                if (isStartedService) {
                    JivoWebSocketService.stop(sdkContext.appContext)
                }
            }
        }
        Jivo.d("JivoLifecycle: Stop SDK")
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