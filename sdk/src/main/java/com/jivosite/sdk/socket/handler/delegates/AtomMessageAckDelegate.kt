package com.jivosite.sdk.socket.handler.delegates

import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.history.HistoryRepository
import com.jivosite.sdk.socket.handler.SocketMessageDelegate
import javax.inject.Inject

/**
 * Created on 12/5/20.
 *
 * Example of message to handle:
 * {"type":"atom/message.ack","data":"162.1607937882"}
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class AtomMessageAckDelegate @Inject constructor(
    private val historyRepository: HistoryRepository
) : SocketMessageDelegate {

    companion object {
        const val TYPE = "atom/message.ack"
    }

    override fun handle(message: SocketMessage) {
        message.data?.takeIf { it.isNotBlank() }?.run {
            historyRepository.markAsDelivered(this)
        }
    }
}