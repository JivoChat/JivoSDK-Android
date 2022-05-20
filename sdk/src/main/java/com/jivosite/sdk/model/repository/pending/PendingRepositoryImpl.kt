package com.jivosite.sdk.model.repository.pending

import com.jivosite.sdk.model.pojo.message.ClientMessage
import com.jivosite.sdk.model.repository.StateRepository
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.vm.StateLiveData
import com.squareup.moshi.Moshi
import javax.inject.Inject

/**
 * Created on 27.04.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class PendingRepositoryImpl @Inject constructor(
    schedulers: Schedulers,
    private val storage: SharedStorage,
    private val moshi: Moshi
) : StateRepository<PendingState>(schedulers, "Pending", PendingState()), PendingRepository {

    init {
        initMessage()
    }

    override val observableState: StateLiveData<PendingState>
        get() = _stateLive


    private fun initMessage() = updateStateInRepositoryThread {
        doBefore { storage.clientMessage.isNotBlank() }
        transform { state -> state.copy(message = fromJsonClientMessage(storage.clientMessage)) }
    }

    override fun addMessage(clientMessage: ClientMessage) = updateStateInRepositoryThread {
        transform { state -> state.copy(message = clientMessage) }
        doAfter { storage.clientMessage = toJsonClientMessage(clientMessage) }
    }

    override fun removeMessage() = updateStateInRepositoryThread {
        transform { state -> state.copy(message = null) }
        doAfter { storage.clientMessage = "" }
    }

    override fun clear() = updateStateInRepositoryThread {
        doBefore { state -> state.message != null }
        transform { PendingState() }
        doAfter { storage.clientMessage = "" }
    }

    private fun toJsonClientMessage(clientMessage: ClientMessage): String {
        return moshi.adapter(ClientMessage::class.java).toJson(clientMessage)
    }

    private fun fromJsonClientMessage(clientMessage: String): ClientMessage? {
        return moshi.adapter(ClientMessage::class.java).fromJson(clientMessage)
    }

}