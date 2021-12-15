package com.jivosite.sdk.di.service

import com.jivosite.sdk.di.service.modules.WebSocketServiceModule
import com.jivosite.sdk.di.service.modules.SocketMessageHandlerModule
import com.jivosite.sdk.di.service.modules.StateModule
import com.jivosite.sdk.socket.JivoWebSocketService
import dagger.Subcomponent

/**
 * Created on 10.09.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
@ServiceScope
@Subcomponent(
    modules = [
        WebSocketServiceModule::class,
        StateModule::class,
        SocketMessageHandlerModule::class
    ]
)
interface WebSocketServiceComponent {
    fun inject(service: JivoWebSocketService)
}