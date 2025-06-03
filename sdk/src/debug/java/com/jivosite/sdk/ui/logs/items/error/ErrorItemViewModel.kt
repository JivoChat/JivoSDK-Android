package com.jivosite.sdk.ui.logs.items.error

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
class ErrorItemViewModel @Inject constructor() : LogsItemViewModel() {

    override val text: LiveData<String> = _message.map {
        when (it) {
            is LogMessage.Error -> "error: ${it.cause.localizedMessage}"
            else -> "Unknown"
        }
    }
}