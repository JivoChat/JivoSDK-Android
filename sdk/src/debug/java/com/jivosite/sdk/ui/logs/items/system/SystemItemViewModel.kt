package com.jivosite.sdk.ui.logs.items.system

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jivosite.sdk.logger.LogMessage
import com.jivosite.sdk.ui.logs.items.LogsItemViewModel
import javax.inject.Inject

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class SystemItemViewModel @Inject constructor() : LogsItemViewModel() {

    val logMessage: LiveData<LogMessage>
        get() = _message

    override val text: LiveData<String> = Transformations.map(_message) {
        when (it) {
            is LogMessage.LoadConfig -> "load config...\n${it.url}"
            is LogMessage.Connecting -> "connecting...\n${it.uri}"
            is LogMessage.Connected -> "connected"
            is LogMessage.Disconnected -> "disconnected"
            is LogMessage.Ping -> """ping (message="${it.message}")"""
            is LogMessage.Pong -> """pong (message="${it.message}")"""
            else -> "Unknown"
        }
    }
}