package com.jivosite.sdk.di.modules

import android.content.Context
import com.jivosite.sdk.BuildConfig
import com.jivosite.sdk.logger.DebugLogger
import com.jivosite.sdk.logger.Logger
import com.jivosite.sdk.logger.ReleaseLogger
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.socket.transmitter.DefaultTransmitter
import com.jivosite.sdk.socket.transmitter.Transmitter
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.async.SchedulersImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created on 02.09.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
@Module
class SdkModule(appContext: Context, widgetId: String) {

    private val sdkContext = SdkContext(appContext, widgetId)

    @Provides
    @Singleton
    fun provideSdkContext(): SdkContext = sdkContext

    @Provides
    @Singleton
    fun provideSchedulers(): Schedulers = SchedulersImpl()

    @Provides
    @Singleton
    fun provideSharedStorage(): SharedStorage {
        return SharedStorage(sdkContext.appContext)
    }

    @Provides
    @Singleton
    fun provideLogger(debugLogger: DebugLogger): Logger {
        return if (BuildConfig.DEBUG) {
            debugLogger
        } else {
            ReleaseLogger()
        }
    }

    @Provides
    @Singleton
    fun provideTransmitter(transmitter: DefaultTransmitter): Transmitter {
        return transmitter
    }
}