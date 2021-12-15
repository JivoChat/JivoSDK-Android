package com.jivosite.sdk.api

import androidx.lifecycle.LiveData
import com.jivosite.sdk.model.pojo.file.AccessResponse
import com.jivosite.sdk.model.pojo.media.MediaSignResponse
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
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
interface MediaApi {

    @Deprecated("not used for new media service")
    @GET("/api/1.0/sites/{siteId}/widgets/{widgetPublicId}/media/transfer/access/gain?allowContentType=1")
    @Headers("$URL:$API")
    fun getAccessForFile(
        @Path("siteId") siteId: Long,
        @Path("widgetPublicId") widgetPublicId: String,
        @Query("extension") extension: String,
        @Query("type") mimeType: String,
    ): LiveData<ApiResponse<AccessResponse>>

    @Deprecated("not used for new media service")
    @Multipart
    @POST
    fun uploadFile(
        @Url url: String?,
        @PartMap map: HashMap<String, RequestBody>,
        @Part body: MultipartBody.Part,
    ): LiveData<ApiResponse<Void>>

    @HEAD
    fun getMedia(@Url path: String): LiveData<ApiResponse<Void>>

    @GET("/api/1.0/auth/media/sign/put")
    @Headers("$URL:$API")
    fun getSign(
        @Query("file_name") fileName: String,
        @Query("client_id") clientId: String,
        @Query("site_id") siteId: Long,
        @Query("public_id") widgetId: String,
    ): LiveData<ApiResponse<MediaSignResponse>>

    @PUT
    fun uploadMedia(
        @Header("x-metadata") metadata: String?,
        @Url url: String?,
        @Body body: RequestBody,
    ): LiveData<ApiResponse<Void>>
}
