package com.jivosite.sdk.model.repository.history

import com.jivosite.sdk.model.pojo.message.HistoryMessage
import java.util.*

/**
 * Created on 12/14/20.
 *
 * Описывает состояние истории сообщений. То есть тех, которые есть на сервере. Если сообщение еще не отправлено,
 * то его здесь быть не должно.
 *
 * @param messages список сообщений в истории, которые пришли от сервера.
 * @param hasUnread индикатор того, что в истории есть не прочитанные сообщения.
 * @param lastReadMsgId идентификатор последнего просмотренного сообщения.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
data class HistoryState(
    val messages: List<HistoryMessage> = Collections.emptyList(),
    val hasUnread: Boolean = false,
    val lastReadMsgId: Long = 0
) {
    companion object {
        private const val CUSTOM_TO_STRING = true
    }

    override fun toString(): String {
        return if (CUSTOM_TO_STRING) {
            val msg = when {
                messages.isEmpty() -> "\"empty\""
                messages.size == 1 -> messages[0].toString()
                else -> {
                    """"${messages.first()}"..."${messages.last()}""""
                }
            }
            """HistoryState{messages=$msg, unread=$hasUnread, read=$lastReadMsgId}"""
        } else {
            super.toString()
        }
    }
}
