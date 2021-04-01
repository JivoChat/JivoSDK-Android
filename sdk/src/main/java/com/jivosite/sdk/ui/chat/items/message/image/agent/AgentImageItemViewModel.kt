package com.jivosite.sdk.ui.chat.items.message.image.agent

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.ui.chat.items.AgentMessageEntry
import com.jivosite.sdk.ui.chat.items.message.general.MessageItemViewModel
import java.io.File
import javax.inject.Inject

/**
 * Created on 16.09.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class AgentImageItemViewModel @Inject constructor(
    agentRepository: AgentRepository
) : MessageItemViewModel<AgentMessageEntry>(agentRepository)  {

    val imageUrl: LiveData<String> = Transformations.map(_entry) { entry ->
        entry?.message?.data ?: ""
    }

    val imageName: String
        get() = File(Uri.parse(_entry.value?.message?.data).path ?: "").name
}