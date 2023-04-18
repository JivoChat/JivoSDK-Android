package com.jivosite.sdk.di

import com.jivosite.sdk.api.SdkApi
import com.jivosite.sdk.api.TelemetryApi
import com.jivosite.sdk.di.modules.*
import com.jivosite.sdk.di.service.WebSocketServiceComponent
import com.jivosite.sdk.di.service.modules.SocketMessageHandlerModule
import com.jivosite.sdk.di.service.modules.StateModule
import com.jivosite.sdk.di.service.modules.WebSocketServiceModule
import com.jivosite.sdk.di.ui.chat.JivoChatComponent
import com.jivosite.sdk.di.ui.chat.JivoChatFragmentModule
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.repository.contacts.ContactFormRepository
import com.jivosite.sdk.model.repository.history.HistoryRepository
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.push.RemoteMessageHandler
import com.jivosite.sdk.support.usecase.ClearUseCase
import com.jivosite.sdk.support.usecase.HistoryUseCase
import com.jivosite.sdk.support.usecase.UpdatePushTokenUseCase
import com.jivosite.sdk.ui.views.JivoChatButton
import com.squareup.moshi.Moshi
import dagger.Component
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Created on 02.09.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
@Singleton
@Component(
    modules = [
        SdkModule::class,
        ParseModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        DbModule::class,
        PushMessageHandlerModule::class
    ]
)
interface JivoSdkComponent {

    fun serviceComponent(
        serviceModule: WebSocketServiceModule,
        stateModule: StateModule,
        handlerModule: SocketMessageHandlerModule
    ): WebSocketServiceComponent

    fun chatComponent(module: JivoChatFragmentModule): JivoChatComponent

    fun telemetryApi(): TelemetryApi

    fun storage(): SharedStorage

    fun sdkContext(): SdkContext

    fun updatePushTokenUseCaseProvider(): Provider<UpdatePushTokenUseCase>

    fun sdkApi(): SdkApi

    fun remoteMessageHandler(): RemoteMessageHandler

    fun clearUseCaseProvider(): Provider<ClearUseCase>

    fun inject(button: JivoChatButton)

    fun historyRepository(): HistoryRepository

    fun moshi(): Moshi

    fun contactFormRepository(): ContactFormRepository

    fun historyUseCase(): Provider<HistoryUseCase>
}