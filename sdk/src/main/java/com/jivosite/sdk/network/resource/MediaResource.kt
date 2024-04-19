package com.jivosite.sdk.network.resourceimport androidx.annotation.MainThreadimport androidx.lifecycle.LiveDataimport androidx.lifecycle.MediatorLiveDataimport com.jivosite.sdk.Jivoimport com.jivosite.sdk.model.pojo.file.JivoMediaFileimport com.jivosite.sdk.model.pojo.media.MediaSignResponseimport com.jivosite.sdk.model.pojo.response.Responseimport com.jivosite.sdk.network.response.ApiResponseimport com.jivosite.sdk.network.retrofit.CountingRequestBodyimport com.jivosite.sdk.network.retrofit.error.JivoApiExceptionimport com.jivosite.sdk.support.async.Schedulersimport com.jivosite.sdk.support.ext.asRequestBodyimport okhttp3.MediaType.Companion.toMediaTypeimport okhttp3.RequestBody/** * Created on 30.10.2021. * * @author Alexander Tavtorkin (tavtorkin@jivosite.com) */abstract class MediaResource(schedulers: Schedulers) {    private val result = MediatorLiveData<Resource<String>>()    init {        schedulers.ui.execute {            setValue(Resource.loading())            val accessSource: LiveData<ApiResponse<MediaSignResponse>> = getAccess()            result.addSource(accessSource) { accessResponse ->                result.removeSource(accessSource)                handleApiResponse(accessResponse)?.let { apiResponse ->                    schedulers.io.execute {                        val file = file()                        if (file.inputStream != null) {                            val fileName = file.name                            val mimeTypeMap = file.mimeType                            val body = CountingRequestBody(file.inputStream.asRequestBody(mimeTypeMap.toMediaType())) { value ->                                result.postValue(Resource.progressUpdate(value))                            }                            val (metadata, host, url) = (apiResponse.body as MediaSignResponse).run {                                Triple(metadata, host, """$host/$fileName?ts=$ts&sign=$sign&public""")                            }                            schedulers.ui.execute {                                val uploadSource = upload(metadata, url, body)                                result.addSource(uploadSource) { uploadResponse ->                                    result.removeSource(uploadSource)                                    handleApiResponse(uploadResponse)?.let { response ->                                        setValue(                                            response.headers?.get("Location")?.get(0)?.let {                                                Resource.success("$host$it")                                            } ?: Resource.error(                                                "There is no location header in upload response",                                                JivoApiException(mapOf("empty_headers" to ""))                                            ))                                    }                                }                            }                        } else {                            result.postValue(                                Resource.error(                                    "Can not read file",                                    JivoApiException(mapOf("can_not_read" to ""))                                )                            )                        }                    }                }            }        }    }    private fun <T> handleApiResponse(apiResponse: ApiResponse<T>?): ApiResponse<T>? {        return if (apiResponse != null) {            if (apiResponse.isSuccessful) {                val body: T? = apiResponse.body                if (body == null) {                    return apiResponse                } else if (body is Response) {                    if (body.isOk) {                        return apiResponse                    } else {                        val errors = HashMap<String, String>()                        body.errorList?.forEach {                            errors[it] = ""                        }                        val error = JivoApiException(errors)                        setValue(Resource.error(error.localizedMessage, error))                        null                    }                } else {                    setValue(                        Resource.error(                            "Response body not instantiate Response class",                            JivoApiException(mapOf("wrong_body_class" to ""))                        )                    )                    null                }            } else {                val error = apiResponse.throwable                setValue(Resource.error(error?.localizedMessage, error))                null            }        } else {            Jivo.e("There is something wrong in UploadResource")            setValue(Resource.error(null, null))            null        }    }    @MainThread    private fun setValue(newValue: Resource<String>) {        if (result.value != newValue) {            result.value = newValue        }    }    fun asLiveData() = result as LiveData<Resource<String>>    protected abstract fun getAccess(): LiveData<ApiResponse<MediaSignResponse>>    protected abstract fun file(): JivoMediaFile    protected abstract fun upload(        metadata: String?,        url: String,        body: RequestBody,    ): LiveData<ApiResponse<Void>>    class Builder(private val schedulers: Schedulers) {        private var getAccess: (() -> LiveData<ApiResponse<MediaSignResponse>>)? = null        private var jivoMediaFile: (() -> JivoMediaFile)? = null        private var upload: ((String?, String, RequestBody) -> LiveData<ApiResponse<Void>>)? =            null        fun getAccess(call: () -> LiveData<ApiResponse<MediaSignResponse>>): Builder {            this.getAccess = call            return this        }        fun file(call: () -> JivoMediaFile): Builder {            this.jivoMediaFile = call            return this        }        fun upload(call: (String?, String, RequestBody) -> LiveData<ApiResponse<Void>>): Builder {            this.upload = call            return this        }        fun build(): MediaResource {            return object : MediaResource(schedulers) {                override fun getAccess(): LiveData<ApiResponse<MediaSignResponse>> = requireNotNull(getAccess) {                    "You need to declare getAccess method"                }.invoke()                override fun file(): JivoMediaFile = requireNotNull(jivoMediaFile) {                    "You need to declare file method"                }.invoke()                override fun upload(                    metadata: String?,                    url: String,                    body: RequestBody,                ): LiveData<ApiResponse<Void>> {                    return requireNotNull(upload) { "You need to declare upload method" }.invoke(metadata, url, body)                }            }        }    }}