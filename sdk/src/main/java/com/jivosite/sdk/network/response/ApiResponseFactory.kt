package com.jivosite.sdk.network.response

import com.jivosite.sdk.network.retrofit.error.ErrorResponse
import com.jivosite.sdk.network.retrofit.error.JivoApiException
import com.squareup.moshi.Moshi
import retrofit2.Response

/**
 * Created on 17.11.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class ApiResponseFactory(val moshi: Moshi) : ApiResponse.Factory {

    override fun <T> create(response: Response<T>): ApiResponse<T> {
        return if (response.isSuccessful) {
            ApiResponse(response.code(), response.body(), response.headers().toMultimap())
        } else {
            val errorResponse = response.errorBody()!!.string()
            val error = moshi.adapter(ErrorResponse::class.java).fromJson(errorResponse)
            ApiResponse(response.code(), JivoApiException(error!!.violationList))
        }
    }

    override fun <T> create(throwable: Throwable): ApiResponse<T> = ApiResponse(throwable)
}
