package com.jivosite.sdk.model.repository.send

import com.jivosite.sdk.model.pojo.message.ClientMessage
import com.jivosite.sdk.model.pojo.message.HistoryMessage
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.support.vm.StateLiveData

/**
 * Created on 2/2/21.
 *
 * Репозиторий, который отвечает за список отправляемых сообщений. То есть тех, которые еще
 * не попали в историю переписки.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
interface SendMessageRepository {

    companion object {
        const val SEND_TIMEOUT = 5000L // 5 sec to send message
    }

    val observableState: StateLiveData<SendMessageState>

    /**
     * Добавление сообщения в список для отправки.
     * @param message Сообщение от клиента.
     */
    fun addMessage(message: ClientMessage)

    /**
     * Обработка входящего сообщения типа atom/message.id
     *
     * @param message Сообщение от сервера.
     * @param doAfter Действие, которое необходимо выполнить после удачной обработки.
     */
    fun handleSocketMessage(message: SocketMessage, doAfter: (HistoryMessage) -> Unit)

    fun clear()
}