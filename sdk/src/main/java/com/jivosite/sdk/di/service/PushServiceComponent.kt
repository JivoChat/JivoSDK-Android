package com.jivosite.sdk.di.service

import com.jivosite.sdk.di.service.modules.PushServiceModule
import com.jivosite.sdk.push.JivoFirebaseMessagingService
import dagger.Subcomponent

/**
 * Created on 10.09.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
@ServiceScope
@Subcomponent(
    modules = [
        PushServiceModule::class
    ]
)
interface PushServiceComponent {
    fun inject(service: JivoFirebaseMessagingService)
}