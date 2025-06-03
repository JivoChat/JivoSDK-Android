package com.jivosite.sdk.ui.chat.items.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.jivosite.sdk.ui.chat.items.EventEntry
import javax.inject.Inject

/**
 * Created on 2/9/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class EventItemViewModel @Inject constructor() : ViewModel() {

    private val _event = MutableLiveData<EventEntry>()
    var event: EventEntry? = null
        set(value) {
            field = value
            value?.run { _event.value = this }
        }
        get() = _event.value

    val code: LiveData<Int> = _event.map {
        it.code
    }

    val reason: LiveData<String> = _event.map {
        it.reason
    }
}