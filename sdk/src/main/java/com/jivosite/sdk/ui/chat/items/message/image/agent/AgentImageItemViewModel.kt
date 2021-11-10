package com.jivosite.sdk.ui.chat.items.message.image.agent

import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.model.repository.media.MediaRepository
import com.jivosite.sdk.ui.chat.items.AgentMessageEntry
import com.jivosite.sdk.ui.chat.items.message.media.MediaItemViewModel
import javax.inject.Inject

/**
 * Created on 16.09.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class AgentImageItemViewModel @Inject constructor(
    agentRepository: AgentRepository,
    mediaRepository: MediaRepository
) : MediaItemViewModel<AgentMessageEntry>(agentRepository, mediaRepository)
