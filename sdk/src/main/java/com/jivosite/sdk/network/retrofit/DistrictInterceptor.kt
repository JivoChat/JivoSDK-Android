package com.jivosite.sdk.network.retrofit

import com.jivosite.sdk.Jivo
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

/**
 * Created on 08.04.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
@Deprecated("not used")
class DistrictInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val isRuLocation = Locale.getDefault().language.lowercase() == "ru"
        return if (isRuLocation) {
            val request = chain.request()
            val url = request.url

            val ruHost = url.host.replace(".jivosite.com", ".jivo.ru")
            val ruUrl = url.newBuilder()
                    .host(ruHost)
                    .build()

            Jivo.i("Change district from ${url.host} to ${ruUrl.host}")
            chain.proceed(request.newBuilder().url(ruUrl).build())
        } else {
            chain.proceed(chain.request())
        }
    }
}