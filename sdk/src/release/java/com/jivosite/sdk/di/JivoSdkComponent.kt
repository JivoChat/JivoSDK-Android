package com.jivosite.sdk.di

import com.jivosite.sdk.api.TelemetryApi
import com.jivosite.sdk.di.modules.NetworkModule
import com.jivosite.sdk.di.modules.ParseModule
import com.jivosite.sdk.di.modules.RepositoryModule
import com.jivosite.sdk.di.modules.SdkModule
import com.jivosite.sdk.di.service.PushServiceComponent
import com.jivosite.sdk.di.service.WebSocketServiceComponent
import com.jivosite.sdk.di.service.modules.PushServiceModule
import com.jivosite.sdk.di.service.modules.SocketMessageHandlerModule
import com.jivosite.sdk.di.service.modules.StateModule
import com.jivosite.sdk.di.service.modules.WebSocketServiceModule
import com.jivosite.sdk.di.ui.chat.JivoChatComponent
import com.jivosite.sdk.di.ui.chat.JivoChatFragmentModule
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.ui.views.JivoChatButton
import dagger.Component
import javax.inject.Singleton

/**
 * Created on 02.09.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
@Singleton
@Component(
    modules = [
        SdkModule::class,
        ParseModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        DbModule::class
    ]
)
interface JivoSdkComponent {

    fun serviceComponent(
        serviceModule: WebSocketServiceModule,
        stateModule: StateModule,
        handlerModule: SocketMessageHandlerModule
    ): WebSocketServiceComponent

    fun chatComponent(module: JivoChatFragmentModule): JivoChatComponent

    fun pushComponent(module: PushServiceModule): PushServiceComponent

    fun telemetryApi(): TelemetryApi

    fun storage(): SharedStorage

    fun sdkContext(): SdkContext

    fun inject(button: JivoChatButton)
}