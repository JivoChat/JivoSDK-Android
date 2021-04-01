package com.jivosite.sdk.di.service.modules

import com.jivosite.sdk.di.service.ServiceScope
import com.jivosite.sdk.socket.JivoWebSocketService
import com.jivosite.sdk.socket.endpoint.DefaultSocketEndpointProvider
import com.jivosite.sdk.socket.endpoint.SocketEndpointProvider
import com.jivosite.sdk.socket.states.ServiceStateContext
import com.jivosite.sdk.socket.support.DefaultReconnectStrategy
import com.jivosite.sdk.socket.support.ReconnectStrategy
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * Created on 10.09.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
@Module(includes = [WebSocketServiceModule.Bindings::class])
class WebSocketServiceModule(private val service: JivoWebSocketService) {

    @Module
    interface Bindings {

        @ServiceScope
        @Binds
        fun provideReconnectStrategy(strategy: DefaultReconnectStrategy): ReconnectStrategy

        @Binds
        fun provideSocketEndpointProvider(provider: DefaultSocketEndpointProvider): SocketEndpointProvider
    }

    @ServiceScope
    @Provides
    fun provideService(): JivoWebSocketService = service

    @ServiceScope
    @Provides
    fun provideServiceStateContext(): ServiceStateContext = service
}