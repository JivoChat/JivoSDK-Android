package com.jivosite.sdk.push.handler

import com.jivosite.sdk.model.pojo.push.PushData

/**
 * Created on 1/19/21.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
interface PushMessageDelegate {

    companion object {
        const val MESSAGE = "JV_MESSAGE"

        const val NOTIFICATION_MESSAGE_ID = 1
    }

    fun handle(data: PushData)
}