package com.jivosite.sdk.api

import androidx.lifecycle.LiveData
import com.jivosite.sdk.model.pojo.push.Device
import com.jivosite.sdk.model.pojo.response.Response
import com.jivosite.sdk.network.response.ApiResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created on 1/18/21.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
interface PushApi {

    @POST("/client/{siteId}/{widgetPublicId}/device")
    fun setPushToken(
        @Header("x-jv-client-id") clientId: String,
        @Path("siteId") siteId: Long,
        @Path("widgetPublicId") widgetPublicId: String,
        @Body body: Device
    ): LiveData<ApiResponse<Response>>

}