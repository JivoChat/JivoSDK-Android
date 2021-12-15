package com.jivosite.sdk.model.repository.typing

import java.util.*

/**
 * Created on 2/3/21.
 *
 * Состояние набора сообщений агентами.
 *
 * @param agents Список идентификаторов агентов, которые в данный момент набирают текст.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
data class TypingState(
    val agents: List<TypingInfo> = Collections.emptyList()
)

/**
 * Информация о наборе сообщения агентом.
 *
 * @param agentId Идентификатор агента, набирающего сообщение.
 * @param timestamp Время, когда последний раз была инормация о наборе сообщения.
 * @param insight Текст, набираемый агентом(возможно появится в будущем).
 */
data class TypingInfo(
    val agentId: Long,
    val timestamp: Long,
    val insight: String = ""
)