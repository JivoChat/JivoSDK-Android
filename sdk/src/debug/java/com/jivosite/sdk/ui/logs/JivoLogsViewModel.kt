package com.jivosite.sdk.ui.logs

import androidx.lifecycle.*
import com.jivosite.sdk.logger.LogMessage
import com.jivosite.sdk.logger.LogsRepository
import com.jivosite.sdk.model.repository.connection.ConnectionState
import com.jivosite.sdk.model.repository.connection.ConnectionStateRepository
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.socket.transmitter.Transmitter
import com.jivosite.sdk.ui.logs.items.LogsItem
import com.jivosite.sdk.ui.logs.items.error.ErrorItem
import com.jivosite.sdk.ui.logs.items.message.MessageItem
import com.jivosite.sdk.ui.logs.items.system.SystemItem
import java.util.*
import javax.inject.Inject

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class JivoLogsViewModel @Inject constructor(
    private val storage: SharedStorage,
    private val connectionStateRepository: ConnectionStateRepository,
    private val transmitter: Transmitter,
    logsRepository: LogsRepository
) : ViewModel() {

    val messages: LiveData<List<LogsItem>> = Transformations.map(logsRepository.messages) { list ->
        list?.filter { message ->
            if (storage.doNotShowPings) {
                message !is LogMessage.Ping && message !is LogMessage.Pong
            } else {
                true
            }
        }?.map { message ->
            when (message) {
                is LogMessage.Received, is LogMessage.Sent -> MessageItem(message)
                is LogMessage.Error -> ErrorItem(message)
                else -> SystemItem(message)
            }
        } ?: Collections.emptyList()
    }

    var messageToSend = MutableLiveData("")

    private val sendState = MediatorLiveData<SendState>().apply {
        value = SendState()
        addSource(messageToSend) { value = value?.copy(hasMessage = it.isNotBlank()) }
        addSource(connectionStateRepository.state) {
            value = value?.copy(hasConnection = it is ConnectionState.Connected)
        }
    }
    val canSend: LiveData<Boolean> = Transformations.map(sendState) {
        it.hasMessage && it.hasConnection
    }

    fun send() {
        transmitter.sendMessage(messageToSend.value ?: return)
        messageToSend.value = ""
    }

    data class SendState(
        val hasMessage: Boolean = true,
        val hasConnection: Boolean = true
    )
}