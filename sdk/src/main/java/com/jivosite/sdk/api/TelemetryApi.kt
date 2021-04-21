package com.jivosite.sdk.api

import com.jivosite.sdk.network.retrofit.ChangeUrlInterceptor.Companion.TELEMETRY
import com.jivosite.sdk.network.retrofit.ChangeUrlInterceptor.Companion.URL
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

/**
 * Created on 08.09.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
interface TelemetryApi {

    @GET("/mobile")
    @Headers("$URL:$TELEMETRY")
    suspend fun send(@QueryMap params: Map<String, String>): String
}