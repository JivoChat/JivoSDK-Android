package com.jivosite.sdk.model.repository.typing

import com.jivosite.sdk.model.repository.StateRepository
import com.jivosite.sdk.model.repository.typing.TypingRepository.Companion.TYPING_TIMEOUT
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.vm.StateLiveData
import javax.inject.Inject

/**
 * Created on 2/3/21.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class TypingRepositoryImpl @Inject constructor(
    schedulers: Schedulers
) : StateRepository<TypingState>(schedulers, "Typing", TypingState()), TypingRepository {

    private val cache: MutableMap<Long, TypingInfo> = HashMap()

    override val observableState: StateLiveData<TypingState>
        get() = _stateLive

    override fun addAgent(agentId: Long, insight: String) = updateStateInRepositoryThread {
        transform { state ->
            cache[agentId] = TypingInfo(agentId, System.currentTimeMillis(), insight)
            state.copy(agents = cache.entries.map { it.value })
        }
        doAfter {
            removeAgentByTimeout(agentId)
        }
    }

    override fun removeAgent(agentId: Long) = updateStateInRepositoryThread {
        transform { state ->
            cache.remove(agentId)
            state.copy(agents = cache.entries.map { it.value })
        }
    }

    private fun removeAgentByTimeout(agentId: Long) = updateStateInRepositoryThread(TYPING_TIMEOUT) {
        transform { state ->
            val info = cache[agentId]
            if (info != null && (System.currentTimeMillis() - info.timestamp) > TYPING_TIMEOUT - 100) {
                cache.remove(agentId)
            }
            state.copy(agents = cache.entries.map { it.value })
        }
    }

    override fun clear() = updateStateInRepositoryThread {
        transform { TypingState() }
    }
}