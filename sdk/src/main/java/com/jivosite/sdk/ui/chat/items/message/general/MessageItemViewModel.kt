package com.jivosite.sdk.ui.chat.items.message.general

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.jivosite.sdk.model.pojo.agent.Agent
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.support.ext.toLongOrDefault
import com.jivosite.sdk.support.vm.AbsentLiveData
import com.jivosite.sdk.ui.chat.items.EntryPosition
import com.jivosite.sdk.ui.chat.items.MessageEntry

/**
 * Created on 16.09.2020.
 *
 * Основной класс для сообщений от пользователя или агента.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
open class MessageItemViewModel<T : MessageEntry>(agentRepository: AgentRepository) : ChatEntryViewModel<T>() {

    val position: EntryPosition
        get() = entry?.position ?: EntryPosition.Single

    val agent: LiveData<Agent> = _entry.switchMap { entry ->
        val from = entry.from
        if (from.isNotBlank()) {
            agentRepository.observeAgent(from)
        } else {
            AbsentLiveData.create()
        }
    }

    val avatar: LiveData<String> = agent.map { agent ->
        agent.photo
    }

    val avatarVisibility: LiveData<Boolean> = _entry.map { entry ->
        when (entry.position) {
            is EntryPosition.First, EntryPosition.Single -> true
            else -> false
        }
    }

    val name: LiveData<String> = agent.map { agent ->
        agent.name
    }

    val nameVisibility: LiveData<Boolean> = _entry.map { entry ->
        when (entry.position) {
            is EntryPosition.Last, EntryPosition.Single -> true
            else -> false
        }
    }

    val labelVisibility: LiveData<Boolean> = _entry.map { entry ->
        entry.from.toLongOrDefault(0) < 0
    }

    val time: LiveData<Long> = _entry.map {
        it.time
    }
}
