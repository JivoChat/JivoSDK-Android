package com.jivosite.sdk.di.modules

import com.jivosite.sdk.model.pojo.message.MessageStatus
import com.jivosite.sdk.model.pojo.rate.RateSettings
import com.squareup.moshi.*
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
            .add(RateSettingsTypeJsonAdapter())
            .add(RateSettingsIconJsonAdapter())
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

class RateSettingsTypeJsonAdapter : JsonAdapter<RateSettings.Type>() {

    @FromJson
    override fun fromJson(reader: JsonReader): RateSettings.Type? {
        return if (reader.peek() != JsonReader.Token.NULL) {
            RateSettings.Type.fromValue(reader.nextString())
        } else {
            reader.nextNull()
        }
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: RateSettings.Type?) {
        writer.value(value?.type)
    }
}

class RateSettingsIconJsonAdapter : JsonAdapter<RateSettings.Icon>() {

    @FromJson
    override fun fromJson(reader: JsonReader): RateSettings.Icon? {
        return if (reader.peek() != JsonReader.Token.NULL) {
            RateSettings.Icon.fromValue(reader.nextString())
        } else {
            reader.nextNull()
        }
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: RateSettings.Icon?) {
        writer.value(value?.icon)
    }
}