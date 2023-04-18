package com.jivosite.sdk.support.usecase

import androidx.lifecycle.LiveData
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.api.SdkApi
import com.jivosite.sdk.model.SdkContext
import com.jivosite.sdk.model.pojo.history.HistoryMessage
import com.jivosite.sdk.model.pojo.history.HistoryResult
import com.jivosite.sdk.model.repository.history.HistoryRepository
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.network.resource.NetworkResource
import com.jivosite.sdk.network.resource.Resource
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.ext.loadSilentlyResource
import javax.inject.Inject

/**
 * Created on 18.04.2023.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */

class HistoryUseCase @Inject constructor(
    private val sdkContext: SdkContext,
    private val schedulers: Schedulers,
    private val sdkApi: SdkApi,
    private val storage: SharedStorage,
    private val historyRepository: HistoryRepository,
) : UseCase {

    override fun execute() {
        val clientId = storage.clientId
        if (clientId.isBlank()) return

        val widgetId = storage.widgetId.ifBlank { sdkContext.widgetId }

        schedulers.ui.execute {
            createRequest(clientId, storage.siteId.toLong(), widgetId).loadSilentlyResource {
                result { messages ->
                    messages.lastOrNull()?.let {
                        if (historyRepository.state.lastReadMsgId < it.msgId) {
                            historyRepository.setHasUnreadMessages(true)
                        }
                    }
                }
                error {
                    Jivo.e("An unsuccessful attempt to get history, error - $it")
                }
            }
        }
    }

    private fun createRequest(clientId: String, siteId: Long, widgetId: String): LiveData<Resource<List<HistoryMessage>>> {
        return NetworkResource.Builder<List<HistoryMessage>, HistoryResult>(schedulers)
            .createCall {
                sdkApi.getHistory(clientId, siteId, widgetId)
            }
            .handleResponse {
                it.result?.messages ?: emptyList()
            }
            .build()
            .asLiveData()
    }
}