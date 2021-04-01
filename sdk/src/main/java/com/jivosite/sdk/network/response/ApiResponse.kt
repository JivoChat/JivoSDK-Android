package com.jivosite.sdk.network.response

import retrofit2.Response

/**
 * Created on 17.11.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class ApiResponse<T>(
        val code: Int,
        val body: T? = null,
        val headers: Map<String, List<String>>? = null,
        val throwable: Throwable? = null,
) {

    constructor(throwable: Throwable) : this(500, null, null, throwable)
    constructor(code: Int, throwable: Throwable) : this(code, null, null, throwable)

    val isSuccessful: Boolean
        get() = code in 200..299

    interface Factory {
        fun <T> create(response: Response<T>): ApiResponse<T>
        fun <T> create(throwable: Throwable): ApiResponse<T>
    }
}