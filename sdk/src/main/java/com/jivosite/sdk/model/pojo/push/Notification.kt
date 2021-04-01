package com.jivosite.sdk.model.pojo.push

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created on 1/19/21.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
@JsonClass(generateAdapter = true)
data class Notification(
    @Json(name = "body_loc_key")
    val key: String,
    @Json(name = "body_loc_args")
    val args: List<String>
)