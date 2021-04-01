package com.jivosite.sdk.push.handler

import com.jivosite.sdk.model.pojo.push.PushData

/**
 * Created on 1/19/21.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
interface PushMessageHandler {

    fun handle(data: PushData)
}