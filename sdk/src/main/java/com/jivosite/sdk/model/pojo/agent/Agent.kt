package com.jivosite.sdk.model.pojo.agent

import okhttp3.internal.toLongOrDefault

/**
 * Created on 12/12/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
data class Agent(
    val id: String,
    val status: AgentStatus = AgentStatus.Unknown,
    val name: String = "",
    val title: String = "",
    val photo: String = "",
    val hasOnlineInChat: Boolean = false
)

sealed class AgentStatus {
    object Unknown : AgentStatus()
    object Online : AgentStatus()
    object Offline : AgentStatus()
    data class TextStatus(val message: String) : AgentStatus()

    companion object {
        fun mapFrom(status: String): AgentStatus {
            return when (status) {
                "online" -> Online
                "offline" -> Offline
                else -> TextStatus(status)
            }
        }
    }
}

fun Agent.isBot(): Boolean {
    return this.id.toLongOrDefault(0) < 0
}
