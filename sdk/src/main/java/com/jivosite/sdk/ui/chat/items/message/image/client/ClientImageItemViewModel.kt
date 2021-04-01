package com.jivosite.sdk.ui.chat.items.message.image.client

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
 * Created on 2/16/21.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class ClientImageItemViewModel @Inject constructor(
    agentRepository: AgentRepository
) : MessageItemViewModel<ClientMessageEntry>(agentRepository) {

    val imageUrl: LiveData<String> = Transformations.map(_entry) { entry ->
        entry.message.data
    }

    val imageName: String
        get() = File(Uri.parse(_entry.value?.message?.data).path ?: "").name

    val status: LiveData<MessageStatus> = Transformations.map(_entry) { entry ->
        when (entry) {
            is ClientMessageEntry -> entry.message.status
            else -> throw IllegalArgumentException("There is unhandled type of client text message")
        }
    }
}