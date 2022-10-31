package com.jivosite.sdk.ui.chat.items.message.text.agent

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jivosite.sdk.model.pojo.message.ClientMessage
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.ui.chat.JivoChatViewModel
import com.jivosite.sdk.ui.chat.items.AgentMessageEntry
import com.jivosite.sdk.ui.chat.items.message.general.MessageItemViewModel
import javax.inject.Inject

/**
 * Created on 16.09.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class AgentTextItemViewModel @Inject constructor(
    agentRepository: AgentRepository
) : MessageItemViewModel<AgentMessageEntry>(agentRepository) {

    val messageEntry: LiveData<AgentMessageEntry> = Transformations.map(_entry) { it }

    var jivoChatViewModel: JivoChatViewModel? = null

    fun sendMessage(message: ClientMessage) {
        jivoChatViewModel?.sendMessage(message)
    }
}