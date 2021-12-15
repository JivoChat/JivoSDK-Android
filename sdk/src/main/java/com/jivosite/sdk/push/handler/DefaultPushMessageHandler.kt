package com.jivosite.sdk.push.handler

import com.jivosite.sdk.Jivo
import com.jivosite.sdk.model.pojo.push.PushData

/**
 * Created on 1/19/21.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class DefaultPushMessageHandler(
    private val delegates: Map<String, PushMessageDelegate>
) : PushMessageHandler {

    override fun handle(data: PushData) {
        Jivo.d("""Push message "$data"""")

        val delegate = delegates[data.notification.key]
        if (delegate != null) {
            delegate.handle(data)
        } else {
            Jivo.w("There is no delegate to handle push message")
            Jivo.w(" --> key=${data.notification.key}")
        }
    }
}