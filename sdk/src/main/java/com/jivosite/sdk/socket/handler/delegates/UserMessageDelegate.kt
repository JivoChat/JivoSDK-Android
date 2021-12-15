package com.jivosite.sdk.socket.handler.delegates

import com.jivosite.sdk.model.pojo.message.HistoryMessage
import com.jivosite.sdk.model.pojo.push.PushData
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.model.repository.chat.ChatStateRepository
import com.jivosite.sdk.model.repository.history.HistoryRepository
import com.jivosite.sdk.model.repository.pagination.PaginationRepository
import com.jivosite.sdk.model.repository.profile.ProfileRepository
import com.jivosite.sdk.model.repository.typing.TypingRepository
import com.jivosite.sdk.push.handler.PushMessageHandler
import com.jivosite.sdk.socket.handler.SocketMessageDelegate
import com.jivosite.sdk.socket.transmitter.Transmitter

/**
 * Created on 1/29/21.
 *
 * Общий делегат для обработки сообщений пользователей, как клиента(из истории), так и оператора.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
abstract class UserMessageDelegate constructor(
    private val chatStateRepository: ChatStateRepository,
    private val profileRepository: ProfileRepository,
    private val historyRepository: HistoryRepository,
    private val paginationRepository: PaginationRepository,
    private val typingRepository: TypingRepository,
    private val messageTransmitter: Transmitter,
    private val handler: PushMessageHandler,
    private val agentRepository: AgentRepository
) : SocketMessageDelegate {

    override fun handle(message: SocketMessage) {
        HistoryMessage.mapFrom(message).let { historyMessage ->
            historyRepository.addMessage(historyMessage)
            paginationRepository.handleHistoryMessage()

            if (!profileRepository.isMe(historyMessage.from)) {
                if (chatStateRepository.state.visible) {
                    historyRepository.markAsRead(historyMessage.number)
                    messageTransmitter.sendMessage(SocketMessage.ack(historyMessage.id))
                } else if (historyMessage.number > historyRepository.state.lastReadMsgId) {
                    historyRepository.setHasUnreadMessages(true)
                    handler.handle(
                        PushData.map(
                            agentRepository.getAgent(message.from ?: ""),
                            message
                        )
                    )
                }

                message.from?.toLongOrNull()?.run { typingRepository.removeAgent(this) }
            }
        }
    }
}