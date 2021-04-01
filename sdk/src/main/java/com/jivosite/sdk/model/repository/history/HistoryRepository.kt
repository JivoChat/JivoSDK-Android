package com.jivosite.sdk.model.repository.history

import com.jivosite.sdk.model.pojo.message.HistoryMessage
import com.jivosite.sdk.support.vm.StateLiveData

/**
 * Created on 12/14/20.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
interface HistoryRepository {

    val state: HistoryState

    val observableState: StateLiveData<HistoryState>

    fun addMessage(message: HistoryMessage)

    fun setHasUnreadMessages(hasUnread: Boolean)

    fun needToLoadHistory(msgId: Long): Boolean

    fun markAsRead(msgId: Long)

    fun markAsDelivered(msgId: String)
}