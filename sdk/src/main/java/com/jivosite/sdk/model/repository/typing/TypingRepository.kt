package com.jivosite.sdk.model.repository.typing

import com.jivosite.sdk.support.vm.StateLiveData

/**
 * Created on 2/3/21.
 *
 * Репозиторий, для хранения информации об операторах, набирающих сообщения.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
interface TypingRepository {

    companion object {
        const val TYPING_TIMEOUT = 5000L
    }

    val observableState: StateLiveData<TypingState>

    fun addAgent(agentId: Long, insight: String)

    fun removeAgent(agentId: Long)

    fun clear()
}