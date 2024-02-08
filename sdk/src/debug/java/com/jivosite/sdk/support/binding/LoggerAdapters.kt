package com.jivosite.sdk.support.binding

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.jivosite.sdk.R
import com.jivosite.sdk.logger.LogMessage
import com.jivosite.sdk.ui.logs.items.message.MessageDirection

/**
 * Created on 22.09.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
@BindingAdapter("messageDirection")
fun setMessageStatus(view: AppCompatImageView, direction: MessageDirection?) {
    val iconResId = when (direction) {
        is MessageDirection.Incoming -> R.drawable.jivo_sdk_vic_received_24
        is MessageDirection.Outgoing -> R.drawable.jivo_sdk_vic_sent_24
        else -> 0
    }
    view.setImageResource(iconResId)
}

@BindingAdapter("messageIcon")
fun setMessageIcon(view: AppCompatImageView, message: LogMessage?) {
    val iconResId = when (message) {
        is LogMessage.LoadConfig -> R.drawable.jivo_sdk_vic_connecting_24
        is LogMessage.Connecting -> R.drawable.jivo_sdk_vic_connecting_24
        is LogMessage.Connected -> R.drawable.jivo_sdk_vic_connected_24
        is LogMessage.Disconnected -> R.drawable.jivo_sdk_vic_disconnected_24
        is LogMessage.Ping -> R.drawable.jivo_sdk_vic_sent_24
        is LogMessage.Pong -> R.drawable.jivo_sdk_vic_received_24
        else -> 0
    }
    view.setImageResource(iconResId)
}