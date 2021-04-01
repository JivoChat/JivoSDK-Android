package com.jivosite.sdk.model.pojo.push

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created on 1/18/21.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
@JsonClass(generateAdapter = true)
data class Device(
    @Json(name = "device_id")
    val deviceId: String,
    @Json(name = "platform")
    val platform: String = "android",
    @Json(name = "token")
    val token: String
)
