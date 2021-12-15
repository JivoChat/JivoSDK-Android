package com.jivosite.sdk.socket.handler.delegates

import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.socket.handler.SocketMessageDelegate
import javax.inject.Inject

/**
 * Created on 12/5/20.
 *
 * Example of message to handle:
 * {"type":"atom/user","data":"online","id":"1"} or {"type":"atom/user","data":"offline"}
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class AtomUserDelegate @Inject constructor(
    private val agentRepository: AgentRepository
) : SocketMessageDelegate {

    companion object {
        const val TYPE = "atom/user"
    }

    override fun handle(message: SocketMessage) {
        message.id?.let { agentId ->
            agentRepository.setAgentStatus(agentId, message.data ?: "")
        } ?: agentRepository.setAgentStatusOffline()
    }
}