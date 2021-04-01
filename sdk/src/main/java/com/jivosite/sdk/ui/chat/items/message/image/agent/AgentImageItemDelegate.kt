package com.jivosite.sdk.ui.chat.items.message.image.agent

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.R
import com.jivosite.sdk.support.dg.AdapterDelegate
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.ChatItem.Companion.VT_AGENT_IMAGE
import com.jivosite.sdk.ui.chat.items.ChatItem.Companion.VT_AGENT_TEXT
import javax.inject.Provider

/**
 * Created on 16.09.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class AgentImageItemDelegate(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModelProvider: Provider<AgentImageItemViewModel>
) : AdapterDelegate<ChatEntry>(VT_AGENT_IMAGE, R.layout.dg_item_agent_image) {

    override fun createViewHolder(itemView: View): AdapterDelegateViewHolder<ChatEntry> {
        return AgentImageItemViewHolder(itemView, lifecycleOwner, viewModelProvider.get())
    }
}