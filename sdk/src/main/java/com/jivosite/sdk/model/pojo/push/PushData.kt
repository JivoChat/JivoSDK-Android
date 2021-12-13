package com.jivosite.sdk.model.pojo.push

import com.jivosite.sdk.model.pojo.agent.Agent
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.push.handler.PushMessageDelegate

/**
 * Created on 1/19/21.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
data class PushData(
    val u: U,
    val notification: Notification
) {
    companion object {
        fun map(agent: Agent?, message: SocketMessage?) = PushData(
            U("", agent?.id ?: ""),
            Notification(
                PushMessageDelegate.MESSAGE,
                listOf(
                    agent?.name ?: "",
                    message?.data ?: ""
                )
            )
        )
    }
}