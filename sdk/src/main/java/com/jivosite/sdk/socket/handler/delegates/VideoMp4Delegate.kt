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
 * Created on 2/25/21.
 *
 * Пример сообщения для обработки:
 * {"type":"video/mp4","data":"https://files.dev.jivosite.com/file-transfer/75613/2021-02-25/03764c01438607dd4f74.mp4","id":"1398.1614236995","from":"7"}
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class VideoMp4Delegate @Inject constructor(
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
        const val TYPE = "video/mp4"
    }
}