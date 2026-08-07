package com.jivosite.sdk.model.pojo.jwt

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class JwtPayload(
    @Json(name = "id")
    val id: String?,
    @Json(name = "iat")
    val iat: Long? = null,
    @Json(name = "exp")
    val exp: Long? = null,
)
