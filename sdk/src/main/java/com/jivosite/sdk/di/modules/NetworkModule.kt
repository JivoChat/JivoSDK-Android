package com.jivosite.sdk.di.modules

import com.jivosite.sdk.BuildConfig
import com.jivosite.sdk.api.MediaApi
import com.jivosite.sdk.api.PushApi
import com.jivosite.sdk.api.SdkApi
import com.jivosite.sdk.api.TelemetryApi
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.network.response.ApiResponse
import com.jivosite.sdk.network.response.ApiResponseFactory
import com.jivosite.sdk.network.retrofit.ChangeUrlInterceptor
import com.jivosite.sdk.network.retrofit.LiveDataCallAdapterFactory
import com.jivosite.sdk.support.async.Schedulers
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created on 02.09.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
@Module
class NetworkModule {

    companion object {
        const val READ_TIMEOUT = 30L // seconds
        const val CONNECT_TIMEOUT = 30L // seconds

        const val BASE_URL = "https://jivosite.com/"

        val LOG_LEVEL = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.HEADERS else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    fun provideConverter(moshi: Moshi): Converter.Factory = MoshiConverterFactory.create(moshi)

    @Provides
    @IntoSet
    fun provideLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().apply {
        level = LOG_LEVEL
    }

    @Provides
    @IntoSet
    fun provideDevInterceptor(storage: SharedStorage): Interceptor = ChangeUrlInterceptor(storage)

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptors: Set<@JvmSuppressWildcards Interceptor>): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .apply { interceptors.forEach { addInterceptor(it) } }
        .build()

    @Provides
    fun provideResponseFactory(moshi: Moshi): ApiResponse.Factory = ApiResponseFactory(moshi)

    @Provides
    fun provideCallAdapter(
        schedulers: Schedulers,
        responseFactory: ApiResponse.Factory,
    ): CallAdapter.Factory = LiveDataCallAdapterFactory(schedulers, responseFactory)

    @Singleton
    @Provides
    fun provideJivoRetrofit(client: OkHttpClient, converter: Converter.Factory, callAdapter: CallAdapter.Factory): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addCallAdapterFactory(callAdapter)
            .addConverterFactory(converter)
            .build()

    @Singleton
    @Provides
    fun provideSdkApi(retrofit: Retrofit): SdkApi {
        return retrofit.create(SdkApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTelemetryApi(retrofit: Retrofit): TelemetryApi {
        return retrofit.create(TelemetryApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMediaApi(retrofit: Retrofit): MediaApi {
        return retrofit.create(MediaApi::class.java)
    }

    @Singleton
    @Provides
    fun providePushApi(retrofit: Retrofit): PushApi {
        return retrofit.create(PushApi::class.java)
    }
}