package com.jivosite.sdk.socket.handler.delegates

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.socket.handler.SocketMessageDelegate
import javax.inject.Inject

/**
 * Created on 12/5/20.
 *
 * Example of message to handle:
 * {"type":"atom/user.title","data":"Консультант","id":"1"}
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class AtomUserTitleDelegate @Inject constructor(
    private val agentRepository: AgentRepository
) : SocketMessageDelegate {

    companion object {
        const val TYPE = "atom/user.title"
    }

    override fun handle(message: SocketMessage) {
        message.id?.let { agentId ->
            agentRepository.setAgentTitle(agentId, message.data ?: "")
        }
    }
}