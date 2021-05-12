package com.jivosite.sdk

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.OnLifecycleEvent
import com.jivosite.sdk.api.SdkApi
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.pojo.config.Config
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.network.resource.NetworkResource
import com.jivosite.sdk.network.resource.Resource
import com.jivosite.sdk.socket.JivoWebSocketService
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.ext.loadSilently

/**
 * Created on 19.11.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class JivoLifecycleObserver(
    private val sdkContext: SdkContext,
    private val storage: SharedStorage,
    private val sdkApi: SdkApi,
    private val schedulers: Schedulers
) : LifecycleObserver {

    private var isStartedService = false

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeground() {
        if (storage.startOnInitialization) {
            isStartedService = true
            getSdkConfig().loadSilently()
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

    private fun getSdkConfig(): LiveData<Resource<Boolean>> {
        return NetworkResource.Builder<Boolean, Config>(schedulers)
                .createCall {
                    sdkApi.getConfig(
                            sdkContext.widgetId
                    )
                }
                .handleResponse {
                    storage.siteId = it.siteId
                    storage.chatserverHost = it.chatserverHost
                    storage.apiHost = it.apiHost
                    storage.filesHost = it.filesHost
                    if (isStartedService) {
                        JivoWebSocketService.start(sdkContext.appContext)
                    }
                    it.apiHost.isNotBlank()
                }
                .build()
                .asLiveData()
    }
}