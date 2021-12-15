package com.jivosite.sdk.socket.handler.delegates

import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.history.HistoryRepository
import com.jivosite.sdk.model.repository.send.SendMessageRepository
import com.jivosite.sdk.socket.handler.SocketMessageDelegate
import javax.inject.Inject

/**
 * Created on 12/5/20.
 *
 * Example of message to handle:
 * {"type":"atom/message.id","data":"162.1607937881","context":"CONTEXT_ID"}
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class AtomMessageIdDelegate @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val sendMessageRepository: SendMessageRepository
) : SocketMessageDelegate {

    companion object {
        const val TYPE = "atom/message.id"
    }

    override fun handle(message: SocketMessage) {
        if (!message.data.isNullOrBlank() && !message.context.isNullOrBlank()) {
            sendMessageRepository.handleSocketMessage(message) { historyMessage ->
                historyRepository.addMessage(historyMessage)
            }
        }
    }
}