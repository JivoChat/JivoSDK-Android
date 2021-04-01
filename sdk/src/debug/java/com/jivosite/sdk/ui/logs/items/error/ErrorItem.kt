package com.jivosite.sdk.ui.logs.items.error

import com.jivosite.sdk.logger.LogMessage
import com.jivosite.sdk.ui.logs.items.LogsItem

/**
 * Created on 12/9/20.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class ErrorItem(message: LogMessage) : LogsItem(VT_ERROR, message)