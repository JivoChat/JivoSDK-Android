package com.jivosite.sdk.model.repository.unsupported

import com.jivosite.sdk.model.pojo.message.splitIdTimestamp
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.StateRepository
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.vm.StateLiveData
import java.util.*
import javax.inject.Inject

/**
 * Created on 10.11.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class UnsupportedRepositoryImpl @Inject constructor(
    schedulers: Schedulers,
) : StateRepository<UnsupportedState>(schedulers, "Unsupported", UnsupportedState()),
    UnsupportedRepository {

    private val messagesCache: SortedMap<Long, SocketMessage> = TreeMap { o1, o2 ->
        o1.compareTo(o2)
    }

    override val observableState: StateLiveData<UnsupportedState>
        get() = _stateLive

    override fun addMessage(message: SocketMessage) = updateStateInRepositoryThread {
        transform { state ->
            messagesCache[message.id.splitIdTimestamp().second] = message
            state.copy(messages = messagesCache.entries.map { it.value })
        }
    }

    override fun clear() = updateStateInDispatchingThread {
        transform { UnsupportedState() }
        doAfter { messagesCache.clear() }
    }
}