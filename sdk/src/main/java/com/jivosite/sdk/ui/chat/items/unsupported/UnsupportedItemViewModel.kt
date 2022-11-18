package com.jivosite.sdk.ui.chat.items.unsupported

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.jivosite.sdk.ui.chat.items.UnsupportedEntry
import javax.inject.Inject

/**
 * Created on 10.11.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class UnsupportedItemViewModel @Inject constructor() : ViewModel() {

    private val _data = MutableLiveData<UnsupportedEntry>()
    var data: UnsupportedEntry? = null
        set(value) {
            field = value
            value?.run { _data.value = this }
        }
        get() = _data.value

    val message: LiveData<String> = Transformations.map(_data) { it ->
        it.message.run {
            """{type = ${this.type}, id = ${this.id}, data = ${this.data}""".trimMargin()
        }
    }
}