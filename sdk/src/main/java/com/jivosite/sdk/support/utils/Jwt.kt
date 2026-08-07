package com.jivosite.sdk.support.utils

import com.squareup.moshi.Moshi
import android.util.Base64
import com.jivosite.sdk.model.pojo.jwt.JwtPayload
import com.jivosite.sdk.support.ext.fromJson

private const val JWT_BASE64_FLAGS = Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP

private fun extractPayloadSegment(token: String): String? =
    token.split('.').getOrNull(1)?.takeIf { it.isNotBlank() }

private fun decodePayloadJson(segment: String): String =
    String(Base64.decode(segment, JWT_BASE64_FLAGS), Charsets.UTF_8)

fun Moshi.parseJivoJwtPayload(token: String): Result<JwtPayload?> = runCatching {
    val segment = extractPayloadSegment(token) ?: return@runCatching null
    fromJson<JwtPayload>(decodePayloadJson(segment))
}

fun Moshi.parseJivoJwtUserId(token: String): String? =
    parseJivoJwtPayload(token).getOrNull()?.id?.takeIf { it.isNotBlank() }

/** [exp] is Unix seconds; returns false if [exp] is absent (no expiry in payload). */
fun JwtPayload.isExpired(nowSeconds: Long = System.currentTimeMillis() / 1000, leewaySeconds: Long = 60): Boolean =
    exp?.let { nowSeconds - leewaySeconds > it } == true