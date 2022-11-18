package com.jivosite.sdk.model.repository.unsupported

import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.support.vm.StateLiveData

/**
 * Created on 10.11.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
interface UnsupportedRepository {

    val observableState: StateLiveData<UnsupportedState>

    fun addMessage(message: SocketMessage)

    fun clear()
}