package com.jivosite.sdk.ui.logs.items

import com.jivosite.sdk.logger.LogMessage
import com.jivosite.sdk.support.dg.AdapterDelegateItem

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
open class LogsItem(viewType: Int, data: LogMessage) : AdapterDelegateItem<LogMessage>(viewType, data) {

    companion object {
        const val VT_SYSTEM = VT_ITEM
        const val VT_MESSAGE = VT_ITEM + 1
        const val VT_ERROR = VT_ITEM + 2
    }
}