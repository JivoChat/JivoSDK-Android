package com.jivosite.sdk.network.retrofit.error

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created on 17.11.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
@JsonClass(generateAdapter = true)
data class ErrorResponse(
        @Json(name = "ok")
        val isOk: Boolean,
        @Json(name = "violation_list")
        val violationList: Map<String, String>,
)