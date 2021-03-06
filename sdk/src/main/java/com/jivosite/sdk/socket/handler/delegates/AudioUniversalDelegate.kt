package com.jivosite.sdk.socket.handler.delegates

import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.model.repository.chat.ChatStateRepository
import com.jivosite.sdk.model.repository.history.HistoryRepository
import com.jivosite.sdk.model.repository.pagination.PaginationRepository
import com.jivosite.sdk.model.repository.profile.ProfileRepository
import com.jivosite.sdk.model.repository.typing.TypingRepository
import com.jivosite.sdk.push.handler.PushMessageHandler
import com.jivosite.sdk.socket.transmitter.Transmitter
import javax.inject.Inject

/**
 * Created on 3/1/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class AudioUniversalDelegate @Inject constructor(
    chatStateRepository: ChatStateRepository,
    profileRepository: ProfileRepository,
    historyRepository: HistoryRepository,
    paginationRepository: PaginationRepository,
    typingRepository: TypingRepository,
    messageTransmitter: Transmitter,
    handler: PushMessageHandler,
    agentRepository: AgentRepository
) : UserMessageDelegate(
    chatStateRepository,
    profileRepository,
    historyRepository,
    paginationRepository,
    typingRepository,
    messageTransmitter,
    handler,
    agentRepository
) {

    companion object {
        const val TYPE = "audio/*"
    }
}