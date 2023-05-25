package com.jivosite.sdk.support.usecaseimport androidx.lifecycle.LiveDataimport com.jivosite.sdk.api.SdkApiimport com.jivosite.sdk.model.SdkContextimport com.jivosite.sdk.model.pojo.config.Configimport com.jivosite.sdk.model.repository.rating.RatingRepositoryimport com.jivosite.sdk.model.repository.upload.UploadRepositoryimport com.jivosite.sdk.model.storage.SharedStorageimport com.jivosite.sdk.network.resource.NetworkResourceimport com.jivosite.sdk.network.resource.Resourceimport com.jivosite.sdk.socket.JivoWebSocketServiceimport com.jivosite.sdk.socket.states.items.ConnectingStateimport com.jivosite.sdk.socket.states.items.ErrorStateimport com.jivosite.sdk.support.async.Schedulersimport com.jivosite.sdk.support.ext.loadSilentlyResourceimport javax.inject.Inject/** * Created on 17.05.2021. * * @author Alexander Tavtorkin (tavtorkin@jivosite.com) */class SdkConfigUseCase @Inject constructor(    private val sdkContext: SdkContext,    private val schedulers: Schedulers,    private val storage: SharedStorage,    private val sdkApi: SdkApi,    private val jivoWebSocketService: JivoWebSocketService,    private val uploadRepository: UploadRepository,    private val ratingRepository: RatingRepository) : UseCase {    override fun execute() {        val widgetId = storage.widgetId.ifBlank { sdkContext.widgetId }        if (widgetId.isNotBlank()) {            schedulers.ui.execute {                createRequest(widgetId).loadSilentlyResource {                    result { jivoWebSocketService.changeState(ConnectingState::class.java).start() }                    error { jivoWebSocketService.changeState(ErrorState::class.java).error(it) }                }            }        }    }    private fun createRequest(widgetId: String): LiveData<Resource<Boolean>> {        return NetworkResource.Builder<Boolean, Config>(schedulers)            .createCall {                sdkApi.getConfig(widgetId)            }            .handleResponse {                storage.siteId = it.siteId                storage.chatserverHost = it.chatserverHost                storage.apiHost = it.apiHost                storage.filesHost = it.filesHost                uploadRepository.updateLicense(it.license ?: true)                ratingRepository.setRateSettings(it.rateSettings)                it.apiHost.isNotBlank()            }            .build()            .asLiveData()    }}