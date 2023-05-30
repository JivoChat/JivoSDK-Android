package com.jivosite.sdk.lifecycle

import androidx.lifecycle.*
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.socket.JivoWebSocketService
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
    private val historyUseCase: HistoryUseCase
) : DefaultLifecycleObserver {

    private var isStartedService = false

    override fun onResume(owner: LifecycleOwner) {
        historyUseCase.execute()
    }

    override fun onStop(owner: LifecycleOwner) {
        when {
            sdkContext.widgetId.isBlank() -> {
                Jivo.d("WidgetId is empty, service is turned off")
            }
            storage.blacklistedTime.after() -> {
                Jivo.d("Application moved to background. Blacklisted until ${convertTimeMillisToDateFormat(storage.blacklistedTime)}, service is turned off")
            }
            storage.sanctionedTime.after() -> {
                Jivo.d("Application moved to background. Sanctioned until ${convertTimeMillisToDateFormat(storage.sanctionedTime)}, service is turned off")
            }
            storage.startOnInitialization && isStartedService -> {
                Jivo.d("Application moved to background, stop service")
                JivoWebSocketService.stop(sdkContext.appContext)
                isStartedService = false
            }
        }
        Jivo.d("JivoLifecycle: Stop SDK")
    }

    fun onForeground() {
        when {
            sdkContext.widgetId.isBlank() -> {
                Jivo.d("WidgetId is empty, service is turned off")
            }
            storage.blacklistedTime.after() -> {
                Jivo.d("Blacklisted until ${convertTimeMillisToDateFormat(storage.blacklistedTime)}, service is turned off")
            }
            storage.sanctionedTime.after() -> {
                Jivo.d("Sanctioned until ${convertTimeMillisToDateFormat(storage.sanctionedTime)}, service is turned off")
            }
            storage.startOnInitialization && !isStartedService -> {
                JivoWebSocketService.loadConfig(sdkContext.appContext)
                isStartedService = true
                Jivo.d("SDK moved to foreground, load config")
            }
            else -> {
                Jivo.d("SDK moved to foreground, service is turned off")
            }
        }
    }

    fun onClear() {
        isStartedService = false
        JivoWebSocketService.stop(sdkContext.appContext)
    }
}