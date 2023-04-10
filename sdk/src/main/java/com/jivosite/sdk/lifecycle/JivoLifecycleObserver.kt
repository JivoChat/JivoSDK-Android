package com.jivosite.sdk.lifecycle

import androidx.lifecycle.*
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.socket.JivoWebSocketService
import com.jivosite.sdk.support.utils.after
import com.jivosite.sdk.support.utils.convertTimeMillisToDateFormat

/**
 * Created on 19.11.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class JivoLifecycleObserver(
    private val sdkContext: SdkContext,
    private val storage: SharedStorage
) : LifecycleObserver {

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
            storage.startOnInitialization -> {
                JivoWebSocketService.loadConfig(sdkContext.appContext)
                Jivo.d("SDK moved to foreground, load config")
            }
            else -> {
                Jivo.d("SDK moved to foreground, service is turned off")
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
            storage.sanctionedTime.after() -> {
                Jivo.d("Application moved to background. Sanctioned until ${convertTimeMillisToDateFormat(storage.sanctionedTime)}, service is turned off")
            }
            storage.startOnInitialization -> {
                Jivo.d("Application moved to background, stop service")
                JivoWebSocketService.stop(sdkContext.appContext)
            }
        }
        Jivo.d("JivoLifecycle: Stop SDK")
    }
}