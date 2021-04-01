package com.jivosite.sdk.ui.chat.items.message.file.client

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jivosite.sdk.model.pojo.message.MessageStatus
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.ui.chat.items.ClientMessageEntry
import com.jivosite.sdk.ui.chat.items.message.general.MessageItemViewModel
import java.io.File
import javax.inject.Inject

/**
 * Created on 3/2/21.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class ClientFileItemViewModel @Inject constructor(
    agentRepository: AgentRepository
) : MessageItemViewModel<ClientMessageEntry>(agentRepository) {

    val fileName: LiveData<String> = Transformations.map(_entry) { entry ->
        File(Uri.parse(entry.message.data).path ?: "").name
    }

    val url: String?
        get() = _entry.value?.message?.data

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