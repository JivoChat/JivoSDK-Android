package com.jivosite.sdk.support.ext

import com.squareup.moshi.Moshi

inline fun <reified T> Moshi.toJson(data: T): String {
    return this.adapter(T::class.java).toJson(data)
}

inline fun <reified T> Moshi.fromJson(data: String): T? {
    return if (data.isNotBlank()) this.adapter(T::class.java).fromJson(data) else null
}
