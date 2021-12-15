package com.jivosite.sdk.model.repository.send

import com.jivosite.sdk.model.pojo.message.ClientMessage
import java.util.*

/**
 * Created on 2/2/21.
 *
 * @param messages Список сообщений, которые отправляются на сервер. Если отправить сообщение не удалось,
 * то оно останется в истории, со статусом ошибки.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
data class SendMessageState(
    val messages: List<ClientMessage> = Collections.emptyList()
)
