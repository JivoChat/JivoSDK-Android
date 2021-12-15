package com.jivosite.sdk.network.retrofit

import androidx.lifecycle.LiveData
import com.jivosite.sdk.network.response.ApiResponse
import com.jivosite.sdk.support.async.Schedulers
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created on 17.11.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class LiveDataCallAdapterFactory(
        private val schedulers: Schedulers,
        private val responseFactory: ApiResponse.Factory,
) : CallAdapter.Factory() {

    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (getRawType(returnType) != LiveData::class.java) {
            return null
        }

        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawObservableType = getRawType(observableType)
        require(rawObservableType == ApiResponse::class.java) { "Type must be a Response" }
        require(observableType is ParameterizedType) { "Response must be parameterized" }

        return LiveDataCallAdapter<Any>(
                getParameterUpperBound(0, observableType),
                schedulers,
                responseFactory
        )
    }
}