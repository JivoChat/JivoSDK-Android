package com.jivosite.sdk.di.modules

import com.jivosite.sdk.model.pojo.message.MessageStatus
import com.squareup.moshi.*
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created on 06.09.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
@Module
class ParseModule {

    @Provides
    @Singleton
    fun provideParser(): Moshi {
        return Moshi.Builder()
            .add(MessageStatusJsonAdapter())
            .build()
    }
}

class MessageStatusJsonAdapter {

    @ToJson
    fun toJson(status: MessageStatus): String {
        return "SENDING"
    }

    @FromJson
    fun fromJson(status: String): MessageStatus {
       return MessageStatus.Sending
    }
}