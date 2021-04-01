package com.jivosite.sdk.socket.handler.delegates

import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.socket.handler.SocketMessageDelegate
import javax.inject.Inject

/**
 * Created on 12/5/20.
 *
 * Example of message to handle:
 * {"type":"atom/user.photo","data":"https://files.jivosite.com/avatars/98101/5fcf7ed1dd8a0.jpg","id":"1"}
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class AtomUserPhotoDelegate @Inject constructor(
    private val agentRepository: AgentRepository
) : SocketMessageDelegate {

    companion object {
        const val TYPE = "atom/user.photo"
    }

    override fun handle(message: SocketMessage) {
        message.id?.let { agentId ->
            agentRepository.setAgentPhoto(agentId, message.data ?: "")
        }
    }
}