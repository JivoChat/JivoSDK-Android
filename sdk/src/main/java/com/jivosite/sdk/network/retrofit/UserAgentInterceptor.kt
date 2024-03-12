package com.jivosite.sdk.network.retrofitimport android.content.Contextimport android.os.Buildimport com.jivosite.sdk.BuildConfigimport com.jivosite.sdk.support.utils.getEngineInfoimport okhttp3.Interceptorimport okhttp3.OkHttpimport okhttp3.Response/** * Created on 31.01.2022. * * @author Alexander Tavtorkin (tavtorkin@jivosite.com) */class UserAgentInterceptor(val appContext: Context) : Interceptor {    companion object {        const val USER_AGENT_HEADER = "User-Agent"    }    override fun intercept(chain: Interceptor.Chain): Response {        val requestBuilder = chain.request()            .newBuilder()            .removeHeader(USER_AGENT_HEADER)        "JivoSDK-Android/${BuildConfig.VERSION_NAME} (Mobile; Device=${Build.MANUFACTURER}/${Build.MODEL}; Platform=Android/${Build.VERSION.RELEASE},${Build.VERSION.SDK_INT}; Host=${appContext.packageName}; OkHttp=${OkHttp.VERSION}); Engine=${getEngineInfo()}".let {            requestBuilder.addHeader(USER_AGENT_HEADER, it)        }        return chain.proceed(requestBuilder.build())    }}