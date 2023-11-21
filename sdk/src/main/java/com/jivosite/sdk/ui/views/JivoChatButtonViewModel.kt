package com.jivosite.sdk.ui.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.model.repository.history.HistoryRepository
import javax.inject.Inject

/**
 * Created on 02.02.2021.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class JivoChatButtonViewModel @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val agentRepository: AgentRepository
) : ViewModel() {

    private val _state = MediatorLiveData<ButtonState>().apply {
        value = ButtonState()
        addSource(historyRepository.observableState) {
            value = value?.copy(hasNewMessage = it.hasUnread)
        }
        addSource(agentRepository.hasAgentsOnline) {
            value = value?.copy(isOnline = it)
        }
    }

    val state: LiveData<ButtonState>
        get() = _state

    data class ButtonState(
        val isOnline: Boolean = true,
        val hasNewMessage: Boolean = false
    )
}