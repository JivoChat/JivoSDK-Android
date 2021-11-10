package com.jivosite.sdk.network.resource

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.pojo.response.Response
import com.jivosite.sdk.network.response.ApiResponse
import com.jivosite.sdk.network.retrofit.error.JivoApiException
import com.jivosite.sdk.support.async.Schedulers

/**
 * Created on 2019-05-27.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
typealias ApiCall<T> = () -> LiveData<ApiResponse<T>>
typealias ApiResponseHandler<T, R> = (T) -> R

abstract class NetworkResource<ResultType, RequestType>(schedulers: Schedulers) {

    private val result: MediatorLiveData<Resource<ResultType>> = MediatorLiveData<Resource<ResultType>>()

    init {
        schedulers.ui.execute {
            call()
        }
    }

    private fun call() {
        result.value = Resource.loading()
        val apiSource: LiveData<ApiResponse<RequestType>> = createCall()
        result.addSource(apiSource) { apiResponse ->
            result.removeSource(apiSource)
            if (apiResponse != null) {
                if (apiResponse.isSuccessful) {
                    val body = apiResponse.body
                    if (body is Response) {
                        if (body.errorList == null) {
                            val result = handleResponse(body)
                            this.result.value = Resource.success(result)
                        } else {
                            val errors = HashMap<String, String>().also {
                                body.errorList.forEach { error ->
                                    it[error] = ""
                                }
                            }
                            val error = JivoApiException(errors)
                            result.value = Resource.error(error.localizedMessage, error)
                        }
                    } else {
                        Jivo.e("There is something wrong in body response")
                        result.value = Resource.error(null, null)
                    }
                } else {
                    val error = apiResponse.throwable
                    result.value = Resource.error(error?.localizedMessage, error)
                }
            } else {
                Jivo.e("There is something wrong in NetworkResource")
                result.value = Resource.error(null, null)
            }
        }
    }

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    @MainThread
    protected abstract fun handleResponse(requestType: RequestType): ResultType

    fun asLiveData(): LiveData<Resource<ResultType>> {
        return result
    }

    class Builder<ResultType, RequestType>(private val schedulers: Schedulers) {

        private var apiCall: ApiCall<RequestType>? = null
        private var apiResponseHandler: ApiResponseHandler<RequestType, ResultType>? = null

        fun createCall(supplier: ApiCall<RequestType>): Builder<ResultType, RequestType> {
            apiCall = supplier
            return this
        }

        fun handleResponse(handler: ApiResponseHandler<RequestType, ResultType>): Builder<ResultType, RequestType> {
            apiResponseHandler = handler
            return this
        }

        fun build(): NetworkResource<ResultType, RequestType> {
            val apiCall = this.apiCall
            requireNotNull(apiCall) { Jivo.e("You need to declare create call method") }

            val apiResponseHandler = this.apiResponseHandler
            requireNotNull(apiResponseHandler) { Jivo.e("You need to declare handle response method") }

            return object : NetworkResource<ResultType, RequestType>(schedulers) {
                override fun createCall() = apiCall.invoke()
                override fun handleResponse(requestType: RequestType) = apiResponseHandler(requestType)
            }
        }
    }
}
