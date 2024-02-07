package com.jivosite.sdk.api

import androidx.lifecycle.LiveData
import com.jivosite.sdk.model.pojo.push.Device
import com.jivosite.sdk.model.pojo.response.Response
import com.jivosite.sdk.network.response.ApiResponse
import com.jivosite.sdk.network.retrofit.ChangeUrlInterceptor.Companion.NODE
import com.jivosite.sdk.network.retrofit.ChangeUrlInterceptor.Companion.PUSH
import com.jivosite.sdk.network.retrofit.ChangeUrlInterceptor.Companion.URL
import retrofit2.http.*

/**
 * Created on 1/18/21.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
interface PushApi {

    @POST("/client/{siteId}/{widgetPublicId}/device")
    @Headers("$URL:$NODE")
    fun sendDeviceInfo(
        @Header("x-jv-client-id") clientId: String,
        @Path("siteId") siteId: Long,
        @Path("widgetPublicId") widgetPublicId: String,
        @Body body: Device
    ): LiveData<ApiResponse<Unit>>

    @POST("push/delivery/{site_id}/{client_id}/{push_id}?platform=android")
    @Headers("$URL:$PUSH")
    fun postPushDelivery(
        @Path("site_id") siteId: String,
        @Path("client_id") clientId: String,
        @Path("push_id") pushId: String
    ): LiveData<ApiResponse<Response>>

}