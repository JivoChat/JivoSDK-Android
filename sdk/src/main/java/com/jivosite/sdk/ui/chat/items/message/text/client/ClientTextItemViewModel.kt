package com.jivosite.sdk.ui.chat.items.message.text.client

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jivosite.sdk.model.pojo.message.MessageStatus
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.ui.chat.items.ClientMessageEntry
import com.jivosite.sdk.ui.chat.items.MessageEntry
import com.jivosite.sdk.ui.chat.items.SendingMessageEntry
import com.jivosite.sdk.ui.chat.items.message.general.MessageItemViewModel
import javax.inject.Inject

/**
 * Created on 16.09.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class ClientTextItemViewModel @Inject constructor(
    agentRepository: AgentRepository
) : MessageItemViewModel<MessageEntry>(agentRepository) {

    val text: LiveData<String> = Transformations.map(_entry) { entry ->
        when (entry) {
            is ClientMessageEntry -> entry.message.data
            is SendingMessageEntry -> entry.message.data
            else -> throw IllegalArgumentException("There is unhandled type of client text message")
        }
    }

    val status: LiveData<MessageStatus> = Transformations.map(_entry) { entry ->
        when (entry) {
            is ClientMessageEntry -> entry.message.status
            is SendingMessageEntry -> entry.message.status
            else -> throw IllegalArgumentException("There is unhandled type of client text message")
        }
    }
}