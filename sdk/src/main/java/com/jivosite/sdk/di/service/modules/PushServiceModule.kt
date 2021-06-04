package com.jivosite.sdk.di.service.modules

import com.jivosite.sdk.di.service.ServiceScope
import com.jivosite.sdk.push.JivoFirebaseMessagingService
import com.jivosite.sdk.push.handler.DefaultPushMessageHandler
import com.jivosite.sdk.push.handler.PushMessageDelegate
import com.jivosite.sdk.push.handler.PushMessageHandler
import com.jivosite.sdk.push.handler.delegates.TextMessageDelegate
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey

/**
 * Created on 10.09.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
//@Module(includes = [PushServiceModule.Bindings::class])
@Deprecated("")
@Module
class PushServiceModule(private val service: JivoFirebaseMessagingService) {

//    @Module
//    interface Bindings {
//
//        @Binds
//        @IntoMap
//        @StringKey(PushMessageDelegate.MESSAGE)
//        fun provideTextMessageDelegate(delegate: TextMessageDelegate): PushMessageDelegate
//    }

    @ServiceScope
    @Provides
    fun provideService(): JivoFirebaseMessagingService = service

//    @ServiceScope
//    @Provides
//    fun providePushMessageHandler(
//        delegates: Map<String, @JvmSuppressWildcards PushMessageDelegate>,
//    ): PushMessageHandler {
//        return DefaultPushMessageHandler(delegates)
//    }
}