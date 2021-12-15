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
        is MessageDirection.Incoming -> R.drawable.vic_received_24
        is MessageDirection.Outgoing -> R.drawable.vic_sent_24
        else -> 0
    }
    view.setImageResource(iconResId)
}

@BindingAdapter("messageIcon")
fun setMessageIcon(view: AppCompatImageView, message: LogMessage?) {
    val iconResId = when (message) {
        is LogMessage.Connecting -> R.drawable.vic_connecting_24
        is LogMessage.Connected -> R.drawable.vic_connected_24
        is LogMessage.Disconnected -> R.drawable.vic_disconnected_24
        is LogMessage.Ping -> R.drawable.vic_sent_24
        is LogMessage.Pong -> R.drawable.vic_received_24
        else -> 0
    }
    view.setImageResource(iconResId)
}