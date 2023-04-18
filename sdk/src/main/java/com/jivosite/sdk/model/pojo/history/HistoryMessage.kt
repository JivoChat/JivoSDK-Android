package com.jivosite.sdk.model.pojo.history

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created on 18.04.2023.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */

@JsonClass(generateAdapter = true)
data class HistoryMessage(
    @Json(name = "type")
    val type: String,
    @Json(name = "text")
    val text: String,
    @Json(name = "msg_id")
    val msgId: Long,
    @Json(name = "chat_id")
    val chatId: Long,
    @Json(name = "from")
    val from: String,
    @Json(name = "created_ts")
    val createdTs: Long,
    @Json(name = "from_id")
    val fromId: String
)