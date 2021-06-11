package com.jivosite.sdk.network.retrofit

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.storage.SharedStorage
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created on 2019-12-12.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class DevServerInterceptor(private val storage: SharedStorage) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (storage.host.isBlank()) {
            chain.proceed(chain.request())
        } else {
            val request = chain.request()
            val url = request.url

            val devServerAddress = url.host.replace("api.jivosite.com", storage.host)
            val devUrl = url.newBuilder()
                .host(devServerAddress)
                .build()

            Jivo.i("Change host from ${url.host} to ${devUrl.host}")
            chain.proceed(request.newBuilder().url(devUrl).build())
        }
    }
}