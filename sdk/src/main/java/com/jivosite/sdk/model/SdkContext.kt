package com.jivosite.sdk.model

import android.content.Context
import com.jivosite.sdk.socket.states.ServiceState
import java.util.*

/**
 * Created on 12/5/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
data class SdkContext(
    val appContext: Context,
    val widgetId: String
) {

    val pendingIntent: MutableList<ServiceState> = LinkedList()

    var userToken: String = ""
}