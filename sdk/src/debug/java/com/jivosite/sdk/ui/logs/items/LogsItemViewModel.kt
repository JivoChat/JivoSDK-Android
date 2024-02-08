package com.jivosite.sdk.ui.logs.items

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.jivosite.sdk.logger.LogMessage
import com.jivosite.sdk.support.vm.LiveDataDelegate
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
abstract class LogsItemViewModel {

    companion object {
        val timeFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.US)
    }

    protected val _message = MutableLiveData<LogMessage>()
    var message by LiveDataDelegate(_message, LogMessage.Initial)

    val eventInfo: LiveData<String> = Transformations.map(_message) {
        getDefaultEventInfo(it)
    }

    abstract val text: LiveData<String>

    private fun getDefaultEventInfo(message: LogMessage?): String = when (message) {
        is LogMessage.LoadConfig -> "${timeFormat.format(message.ts)} [${message.id}]"
        is LogMessage.Connecting -> "${timeFormat.format(message.ts)} [${message.id}]"
        is LogMessage.Connected -> "${timeFormat.format(message.ts)} [${message.id}]"
        is LogMessage.Ping -> "${timeFormat.format(message.ts)} [${message.id}]"
        is LogMessage.Pong -> "${timeFormat.format(message.ts)} [${message.id}]"
        is LogMessage.Disconnected -> "${timeFormat.format(message.ts)} [${message.id}] code=${message.code}"
        is LogMessage.Received -> "${timeFormat.format(message.ts)} [${message.id}]"
        is LogMessage.Sent -> "${timeFormat.format(message.ts)} [${message.id}]"
        is LogMessage.Error -> "${timeFormat.format(message.ts)} [${message.id}]"
        else -> ""
    }
}