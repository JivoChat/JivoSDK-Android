package com.jivosite.sdk.telemetry

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.api.TelemetryApi

/**
 * Created on 08.09.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
object Telemetry {

    private val api: TelemetryApi
        get() = Jivo.jivoSdkComponent.telemetryApi()

    suspend fun send(params: Map<String, String>) {
        try {
            val response = api.send(emptyMap())
        } catch (e: Exception) {
            Jivo.e(e.toString())
        }
    }

}