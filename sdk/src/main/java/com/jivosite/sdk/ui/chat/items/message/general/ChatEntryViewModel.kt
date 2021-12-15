package com.jivosite.sdk.ui.chat.items.message.general

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jivosite.sdk.ui.chat.items.ChatEntry

/**
 * Created on 2/20/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
open class ChatEntryViewModel<T : ChatEntry>: ViewModel() {

    protected val _entry = MutableLiveData<T>()
    var entry: T? = null
        set(value) {
            field = value
            value?.run { _entry.value = this }
        }
        get() = _entry.value
}