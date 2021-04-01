package com.jivosite.sdk.api

import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created on 08.09.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
interface TelemetryApi {

    @GET("/mobile")
    suspend fun send(@QueryMap params: Map<String, String>): String
}