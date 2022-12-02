package com.jivosite.sdk.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.socket.JivoWebSocketService
import com.jivosite.sdk.support.usecase.SdkConfigUseCase
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
    private val sdkConfigUseCase: SdkConfigUseCase
) : LifecycleObserver {

    private var isStartedService = false

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeground() {
        when {
            sdkContext.widgetId.isBlank() -> {
                isStartedService = false
                Jivo.d("WidgetId is empty, service is turned off")
            }
            storage.blacklistedTime.after() -> {
                isStartedService = false
                Jivo.d("Blacklisted until ${convertTimeMillisToDateFormat(storage.blacklistedTime)}, service is turned off")
            }
            storage.startOnInitialization -> {
                isStartedService = true
                sdkConfigUseCase.onSuccess {
                    if (isStartedService) {
                        Jivo.d("JivoLifecycle: Start SDK")
                        JivoWebSocketService.start(sdkContext.appContext)
                    }
                }.execute()

                sdkConfigUseCase.onRestart {
                    if (isStartedService) {
                        JivoWebSocketService.restart(sdkContext.appContext)
                    }
                }
                Jivo.d("Application moved to foreground, start service")
            }
            else -> {
                isStartedService = false
                Jivo.d("Application moved to foreground, service is turned off")
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackground() {
        when {
            sdkContext.widgetId.isBlank() -> {
                Jivo.d("WidgetId is empty, service is turned off")
            }
            storage.blacklistedTime.after() -> {
                Jivo.d("Application moved to background. Blacklisted until ${convertTimeMillisToDateFormat(storage.blacklistedTime)}, service is turned off")
            }
            storage.startOnInitialization -> {
                Jivo.d("Application moved to background, stop service")
                JivoWebSocketService.stop(sdkContext.appContext)
            }
        }
        Jivo.d("JivoLifecycle: Stop SDK")
        isStartedService = false
    }
}