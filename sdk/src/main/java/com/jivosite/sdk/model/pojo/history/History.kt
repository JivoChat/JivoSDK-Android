package com.jivosite.sdk.model.pojo.history

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created on 18.04.2023.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */

@JsonClass(generateAdapter = true)
data class History(
    @Json(name = "messages")
    val messages: List<HistoryMessage>
) : HistoryResult()



