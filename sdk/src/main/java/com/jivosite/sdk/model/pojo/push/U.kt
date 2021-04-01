package com.jivosite.sdk.model.pojo.push

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created on 1/19/21.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
@JsonClass(generateAdapter = true)
data class U(
    @Json(name = "push_id")
    val pushId: String,
    @Json(name = "user")
    val user: String
)