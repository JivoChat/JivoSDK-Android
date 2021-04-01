package com.jivosite.sdk.network.retrofit

import androidx.lifecycle.LiveData
import com.jivosite.sdk.network.response.ApiResponse
import com.jivosite.sdk.support.async.Schedulers
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created on 17.11.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class LiveDataCallAdapter<T>(
        private val responseType: Type,
        private val schedulers: Schedulers,
        private val responseFactory: ApiResponse.Factory,
) : CallAdapter<T, LiveData<ApiResponse<T>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<T>): LiveData<ApiResponse<T>> = object : LiveData<ApiResponse<T>>() {

        private val started = AtomicBoolean(false)

        override fun onActive() {
            super.onActive()
            if (started.compareAndSet(false, true)) {
                schedulers.network.execute {
                    try {
                        postValue(responseFactory.create(call.execute()))
                    } catch (t: Throwable) {
                        postValue(responseFactory.create(t))
                    }
                }
            }
        }
    }
}