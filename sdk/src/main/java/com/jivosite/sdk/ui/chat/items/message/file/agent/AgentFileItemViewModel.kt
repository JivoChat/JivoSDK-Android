package com.jivosite.sdk.ui.chat.items.message.file.agent

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.model.repository.media.MediaRepository
import com.jivosite.sdk.ui.chat.items.AgentMessageEntry
import com.jivosite.sdk.ui.chat.items.message.media.MediaItemViewModel
import javax.inject.Inject

/**
 * Created on 3/2/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class AgentFileItemViewModel @Inject constructor(
    agentRepository: AgentRepository,
    mediaRepository: MediaRepository
) : MediaItemViewModel<AgentMessageEntry>(agentRepository, mediaRepository) {

    val type: LiveData<String> = Transformations.map(_entry) {
        it.type
    }
}

