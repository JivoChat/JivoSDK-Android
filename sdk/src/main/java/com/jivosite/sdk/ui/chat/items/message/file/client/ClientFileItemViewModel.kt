package com.jivosite.sdk.ui.chat.items.message.file.client

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jivosite.sdk.model.pojo.message.MessageStatus
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.model.repository.media.MediaRepository
import com.jivosite.sdk.ui.chat.items.ClientMessageEntry
import com.jivosite.sdk.ui.chat.items.message.media.MediaItemViewModel
import javax.inject.Inject

/**
 * Created on 3/2/21.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class ClientFileItemViewModel @Inject constructor(
    agentRepository: AgentRepository,
    mediaRepository: MediaRepository
) : MediaItemViewModel<ClientMessageEntry>(agentRepository, mediaRepository) {

    val type: LiveData<String> = Transformations.map(_entry) {
        it.type
    }

    val status: LiveData<MessageStatus> = Transformations.map(_entry) { entry ->
        when (entry) {
            is ClientMessageEntry -> entry.message.status
            else -> throw IllegalArgumentException("There is unhandled type of client text message")
        }
    }
}
