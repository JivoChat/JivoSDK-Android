package com.jivosite.sdk.model.repository.pending

import com.jivosite.sdk.model.pojo.message.ClientMessage
import com.jivosite.sdk.support.vm.StateLiveData

/**
 * Created on 27.04.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
interface PendingRepository {

    val observableState: StateLiveData<PendingState>

    fun addMessage(clientMessage: ClientMessage)

    fun removeMessage()

    fun clear()
}