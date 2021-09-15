package com.jivosite.sdk.ui.chat.items.message.general

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jivosite.sdk.model.pojo.agent.Agent
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.support.vm.AbsentLiveData
import com.jivosite.sdk.ui.chat.items.EntryPosition
import com.jivosite.sdk.ui.chat.items.MessageEntry

/**
 * Created on 16.09.2020.
 *
 * Основной класс для сообщений от пользователя или агента.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
open class MessageItemViewModel<T : MessageEntry>(agentRepository: AgentRepository) : ChatEntryViewModel<T>() {

    val position: EntryPosition
        get() = entry?.position ?: EntryPosition.Single

    private val agent: LiveData<Agent> = Transformations.switchMap(_entry) { entry ->
        val from = entry.from
        if (from.isNotBlank()) {
            agentRepository.observeAgent(from)
        } else {
            AbsentLiveData.create()
        }
    }

    val avatar: LiveData<String> = Transformations.map(agent) { agent ->
        agent?.photo ?: ""
    }

    val avatarVisibility: LiveData<Boolean> = Transformations.map(_entry) { entry ->
        when (entry.position) {
            is EntryPosition.First, EntryPosition.Single -> true
            else -> false
        }
    }

    val name: LiveData<String> = Transformations.map(agent) { agent ->
        agent?.name ?: ""
    }

    val nameVisibility: LiveData<Boolean> = Transformations.map(_entry) { entry ->
        when (entry.position) {
            is EntryPosition.Last, EntryPosition.Single -> true
            else -> false
        }
    }

    val time: LiveData<Long> = Transformations.map(_entry) {
        it.time
    }
}
