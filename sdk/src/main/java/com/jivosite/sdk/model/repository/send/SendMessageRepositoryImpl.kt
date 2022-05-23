package com.jivosite.sdk.model.repository.send

import com.jivosite.sdk.model.pojo.message.ClientMessage
import com.jivosite.sdk.model.pojo.message.HistoryMessage
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.StateRepository
import com.jivosite.sdk.model.repository.history.HistoryRepository
import com.jivosite.sdk.model.repository.profile.ProfileRepository
import com.jivosite.sdk.model.repository.send.SendMessageRepository.Companion.SEND_TIMEOUT
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.vm.StateLiveData
import javax.inject.Inject

/**
 * Created on 2/2/21.
 *
 * Реализация репозитория для отправки сообщения.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class SendMessageRepositoryImpl @Inject constructor(
    schedulers: Schedulers,
    private val profileRepository: ProfileRepository,
    private val historyRepository: HistoryRepository
) : StateRepository<SendMessageState>(schedulers, "SendMessage", SendMessageState()), SendMessageRepository {

    override val observableState: StateLiveData<SendMessageState>
        get() = _stateLive

    override fun addMessage(message: ClientMessage) = updateStateInRepositoryThread {
        transform { state ->
            val messages = ArrayList<ClientMessage>(state.messages.size + 1)
            messages.addAll(state.messages)
            messages.add(message)
            state.copy(messages = messages)
        }
        doAfter {
            waitIdMessage(message.context)
        }
    }

    private fun waitIdMessage(context: String) = updateStateInRepositoryThread(SEND_TIMEOUT) {
        doBefore { state -> state.messages.find { it.context == context } != null }
        transform { state ->
            val messages = state.messages.map { message ->
                if (message.context == context) {
                    message.toError()
                } else {
                    message
                }
            }
            state.copy(messages = messages)
        }
    }

    override fun handleSocketMessage(message: SocketMessage, doAfter: (HistoryMessage) -> Unit) = updateStateInRepositoryThread {
        val messageContext = message.context
        doBefore { state ->
            if (messageContext == null) {
                false
            } else {
                state.messages.find { message -> message.context == messageContext } != null
            }
        }
        transform { state ->
            val messages = ArrayList<ClientMessage>(state.messages.size - 1)
            state.messages.forEach {
                if (it.context == messageContext) {
                    val historyMessage = it.toHistoryMessage(message.data ?: "", profileRepository.id)
                    if (historyRepository.state.messages.isEmpty()) {
                        historyRepository.markAsRead(historyMessage.number)
                    }
                    doAfter(historyMessage)
                } else {
                    messages.add(it)
                }
            }
            state.copy(messages = messages)
        }
    }

    override fun clear() = updateStateInRepositoryThread {
        transform { SendMessageState() }
    }
}
