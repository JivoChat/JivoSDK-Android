package com.jivosite.sdk.socket.handler.delegates

import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.history.HistoryRepository
import com.jivosite.sdk.model.repository.pagination.PaginationRepository
import com.jivosite.sdk.socket.handler.SocketMessageDelegate
import com.jivosite.sdk.socket.transmitter.Transmitter
import javax.inject.Inject

/**
 * Created on 12/5/20.
 *
 * Пример сообщения для обработки:
 * {"type":"atom/me.history","data":"147"}
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class AtomMeHistoryDelegate @Inject constructor(
    private val historyRepository: HistoryRepository,
    private val paginationRepository: PaginationRepository,
    private val messageTransmitter: Transmitter
) : SocketMessageDelegate {

    companion object {
        const val TYPE = "atom/me.history"
    }

    override fun handle(message: SocketMessage) {
        val msgId = message.data?.toLongOrNull()
        if (msgId != null) {
            paginationRepository.setHasNextPage(true)
            if (historyRepository.state.lastReadMsgId < msgId) {
                historyRepository.setHasUnreadMessages(true)
            }
            if (historyRepository.needToLoadHistory(msgId)) {
                paginationRepository.loadingStarted()
                messageTransmitter.sendMessage(SocketMessage.history())
            }
        } else {
            paginationRepository.setHasNextPage(false)
        }
    }
}