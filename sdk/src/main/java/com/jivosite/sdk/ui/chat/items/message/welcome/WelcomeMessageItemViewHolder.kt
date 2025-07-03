package com.jivosite.sdk.ui.chat.items.message.welcome

import android.view.View
import android.widget.TextView
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.R
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.chat.items.ChatEntry

/**
 * Created on 30.11.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class WelcomeMessageItemViewHolder(itemView: View) : AdapterDelegateViewHolder<ChatEntry>(itemView) {

    init {
        val welcomeTextView = itemView.findViewById<TextView>(R.id.welcome)
        welcomeTextView.text = Jivo.getConfig().welcomeMessage.asString(context.resources, context.getString(R.string.welcome_message_placeholder))
    }
}