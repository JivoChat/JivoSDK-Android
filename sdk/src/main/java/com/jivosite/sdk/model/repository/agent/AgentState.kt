package com.jivosite.sdk.model.repository.agent

import com.jivosite.sdk.model.pojo.agent.Agent
import java.util.*

/**
 * Created on 2/4/21.
 *
 * Информация об агентах.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
data class AgentState(
    val hasOnlineAgentsInChat: Boolean = false,
    val agents: List<Agent> = Collections.emptyList()
)
