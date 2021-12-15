package com.jivosite.sdk.socket.handler.delegates

import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.typing.TypingRepository
import com.jivosite.sdk.socket.handler.SocketMessageDelegate
import javax.inject.Inject

/**
 * Created on 12/5/20.
 *
 * Пример сообщения для обработки:
 * {"type":"atom/user.typing","data":"…","id":"6"}
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class AtomUserTypingDelegate @Inject constructor(
    private val typingRepository: TypingRepository
) : SocketMessageDelegate {

    companion object {
        const val TYPE = "atom/user.typing"
    }

    override fun handle(message: SocketMessage) {
        val agentId = message.id?.toLongOrNull() ?: return
        typingRepository.addAgent(agentId, message.data ?: "")
    }
}