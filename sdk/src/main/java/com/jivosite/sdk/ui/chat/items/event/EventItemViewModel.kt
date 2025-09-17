package com.jivosite.sdk.ui.chat.items.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.jivosite.sdk.logger.LogMessage
import com.jivosite.sdk.ui.chat.items.EventEntry
import com.jivosite.sdk.ui.chat.items.message.general.ChatEntryViewModel
import javax.inject.Inject

/**
 * Created on 2/9/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class EventItemViewModel @Inject constructor() : ChatEntryViewModel<EventEntry>() {

   private val logMessage: LiveData<LogMessage.Disconnected> = _entry.map {
        it.logMessage as LogMessage.Disconnected
    }

    val code: LiveData<Int> = logMessage.map {
        it.code
    }

    val reason: LiveData<String> = logMessage.map {
        it.reason
    }
}