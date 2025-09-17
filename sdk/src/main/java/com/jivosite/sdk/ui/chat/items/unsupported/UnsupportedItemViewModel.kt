package com.jivosite.sdk.ui.chat.items.unsupported

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.jivosite.sdk.ui.chat.items.UnsupportedEntry
import com.jivosite.sdk.ui.chat.items.message.general.ChatEntryViewModel
import javax.inject.Inject

/**
 * Created on 10.11.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class UnsupportedItemViewModel @Inject constructor() : ChatEntryViewModel<UnsupportedEntry>() {

    val message: LiveData<String> = _entry.map {
        it.message.run {
            """{type = ${this.type}, id = ${this.id}, data = ${this.data}""".trimMargin()
        }
    }
}