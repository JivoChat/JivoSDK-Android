package com.jivosite.sdk.model.pojo.history

import com.jivosite.sdk.model.pojo.response.Response
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created on 18.04.2023.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */

@JsonClass(generateAdapter = true)
open class HistoryResult(
    @Json(name = "result")
    open val result: History? = null,
) : Response()