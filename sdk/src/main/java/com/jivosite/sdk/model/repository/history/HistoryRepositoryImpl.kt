package com.jivosite.sdk.model.repository.history

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.pojo.message.HistoryMessage
import com.jivosite.sdk.model.pojo.message.MessageStatus
import com.jivosite.sdk.model.pojo.message.splitIdTimestamp
import com.jivosite.sdk.model.repository.StateRepository
import com.jivosite.sdk.model.repository.profile.ProfileRepository
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.utils.transformMessageIfNeed
import com.jivosite.sdk.support.vm.StateLiveData
import java.util.*
import javax.inject.Inject

/**
 * Created on 12/14/20.
 *
 * Репозиторий для работы с историей сообщений, которые приходят с сервера.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class HistoryRepositoryImpl @Inject constructor(
    schedulers: Schedulers,
    private val storage: SharedStorage,
    private val profileRepository: ProfileRepository
) : StateRepository<HistoryState>(schedulers, "History", HistoryState(lastReadMsgId = storage.lastReadMsgId)),
    HistoryRepository {

    private val messagesCache: SortedMap<Long, HistoryMessage> = TreeMap { o1, o2 ->
        o1.compareTo(o2)
    }

    override val state: HistoryState
        get() = _state

    override val observableState: StateLiveData<HistoryState>
        get() = _stateLive

    /**
     * Добавление сообщения в историю.
     *
     * @param message Сообщение, которое необходимо добавить.
     */
    override fun addMessage(message: HistoryMessage) = updateStateInRepositoryThread {
        transform { state ->
            messagesCache[message.number] = transformMessageIfNeed(message)
            state.copy(messages = messagesCache.entries.map {
                it.value.copy(status = if (it.key <= storage.lastAckMsgId) MessageStatus.Delivered else MessageStatus.Sent)
            })
        }
        doAfter {
            if (profileRepository.isMe(message.from)) {
                markAsRead(message.number)
            }
        }
    }

    override fun setHasUnreadMessages(hasUnread: Boolean) = updateStateInRepositoryThread {
        doBefore { state -> state.hasUnread != hasUnread }
        transform { state ->
            Jivo.onNewMessage(hasUnread)
            state.copy(hasUnread = hasUnread)
        }
    }

    override fun needToLoadHistory(msgId: Long): Boolean {
        return messagesCache.isEmpty() || messagesCache.firstKey() < msgId
    }

    override fun markAsRead(msgId: Long) = updateStateInRepositoryThread {
        doBefore { state -> state.lastReadMsgId < msgId }
        transform { state ->
            Jivo.onNewMessage(false)
            storage.lastReadMsgId = msgId
            state.copy(hasUnread = false, lastReadMsgId = msgId)
        }
    }

    override fun markAsDelivered(msgId: String) = updateStateInRepositoryThread {
        val (number, timestamp) = msgId.splitIdTimestamp()
        storage.lastAckMsgId = number
        doBefore { messagesCache[number] != null }
        transform { state ->
            val messages = state.messages.map { message ->
                if (message.number == number) {
                    message.copy(id = msgId, status = MessageStatus.Delivered).also {
                        messagesCache[it.number] = it
                    }
                } else {
                    if (message.status == MessageStatus.Sent && message.number <= number) {
                        message.copy(status = MessageStatus.Delivered).also {
                            messagesCache[it.number] = it
                        }
                    } else {
                        message
                    }
                }
            }
            state.copy(messages = messages)
        }
    }

    override fun clear() = updateStateInDispatchingThread {
        transform { HistoryState() }
        doAfter { messagesCache.clear() }
    }
}
