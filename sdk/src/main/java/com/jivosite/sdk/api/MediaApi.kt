package com.jivosite.sdk.api

import androidx.lifecycle.LiveData
import com.jivosite.sdk.model.pojo.file.AccessResponse
import com.jivosite.sdk.network.response.ApiResponse
import com.jivosite.sdk.network.retrofit.ChangeUrlInterceptor.Companion.API
import com.jivosite.sdk.network.retrofit.ChangeUrlInterceptor.Companion.URL
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.util.*

/**
 * Created on 28.10.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
interface MediaApi {

    @GET("/api/1.0/sites/{siteId}/widgets/{widgetPublicId}/media/transfer/access/gain?allowContentType=1")
    @Headers("$URL:$API")
    fun getAccessForFile(
        @Path("siteId") siteId: Long,
        @Path("widgetPublicId") widgetPublicId: String,
        @Query("extension") extension: String,
        @Query("type") mimeType: String,
    ): LiveData<ApiResponse<AccessResponse>>

    @Multipart
    @POST
    fun uploadFile(
        @Url url: String?,
        @PartMap map: HashMap<String, RequestBody>,
        @Part body: MultipartBody.Part,
    ): LiveData<ApiResponse<Void>>

    @GET
    fun checkMedia(@Url url: String): LiveData<ApiResponse<Unit>>
}
