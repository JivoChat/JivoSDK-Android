package com.jivosite.sdk.ui.logs.items.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.jivosite.sdk.logger.LogMessage
import com.jivosite.sdk.ui.logs.items.LogsItemViewModel
import javax.inject.Inject

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class MessageItemViewModel @Inject constructor() : LogsItemViewModel() {

    val direction: LiveData<MessageDirection> = _message.map {
        when (it) {
            is LogMessage.Received -> MessageDirection.Incoming
            is LogMessage.Sent -> MessageDirection.Outgoing
            else -> MessageDirection.Unknown
        }
    }

    override val text: LiveData<String> = _message.map {
        when (it) {
            is LogMessage.Received -> it.message
            is LogMessage.Sent -> it.message
            else -> ""
        }.trim()
    }
}

sealed class MessageDirection {
    object Incoming : MessageDirection()
    object Outgoing : MessageDirection()
    object Unknown : MessageDirection()
}