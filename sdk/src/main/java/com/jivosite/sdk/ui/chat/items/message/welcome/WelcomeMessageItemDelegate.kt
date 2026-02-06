package com.jivosite.sdk.ui.chat.items.message.welcome

import android.view.View
import com.jivosite.sdk.R
import com.jivosite.sdk.support.dg.AdapterDelegate
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.ChatItem.Companion.VT_WELCOME
import io.noties.markwon.Markwon
import javax.inject.Provider

/**
 * Created on 30.11.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class WelcomeMessageItemDelegate(
    private val markwonProvider: Provider<Markwon>
) : AdapterDelegate<ChatEntry>(VT_WELCOME, R.layout.dg_item_welcome_message) {

    override fun createViewHolder(itemView: View): AdapterDelegateViewHolder<ChatEntry> {
        return WelcomeMessageItemViewHolder(itemView, markwonProvider.get())
    }
}