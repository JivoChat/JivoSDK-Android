package com.jivosite.sdk

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.socket.JivoWebSocketService
import com.jivosite.sdk.support.usecase.SdkConfigUseCase

/**
 * Created on 19.11.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class JivoLifecycleObserver(
    private val sdkContext: SdkContext,
    private val storage: SharedStorage,
    private val sdkConfigUseCase: SdkConfigUseCase
) : LifecycleObserver {

    private var isStartedService = false

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeground() {
        if (storage.startOnInitialization) {
            isStartedService = true
            sdkConfigUseCase.onSuccess {
                if (isStartedService) {
                    JivoWebSocketService.start(sdkContext.appContext)
                }
            }.execute()

            sdkConfigUseCase.onRestart {
                if (isStartedService) {
                    JivoWebSocketService.restart(sdkContext.appContext)
                }
            }
            Jivo.d("Application moved to foreground, start service")
        } else {
            isStartedService = false
            Jivo.d("Application moved to foreground, service is turned off")
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackground() {
        if (storage.startOnInitialization) {
            Jivo.d("Application moved to background, stop service")
            JivoWebSocketService.stop(sdkContext.appContext)
            isStartedService = false
        }
    }
}