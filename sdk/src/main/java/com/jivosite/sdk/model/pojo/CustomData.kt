package com.jivosite.sdk.model.pojo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created on 01.12.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
@JsonClass(generateAdapter = true)
data class CustomData(
    @Json(name = "content")
    val content: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "link")
    val link: String,
    @Json(name = "key")
    val key: String
)