package com.jivosite.sdk.ui.chat.items.message.text.agent

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.R
import com.jivosite.sdk.support.dg.AdapterDelegate
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.chat.JivoChatViewModel
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.ChatItem.Companion.VT_AGENT_TEXT
import io.noties.markwon.Markwon
import javax.inject.Provider

/**
 * Created on 16.09.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class AgentTextItemDelegate(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModelProvider: Provider<AgentTextItemViewModel>,
    private val jivoChatViewModel: JivoChatViewModel,
    private val markwonProvider: Provider<Markwon>
) : AdapterDelegate<ChatEntry>(VT_AGENT_TEXT, R.layout.dg_item_agent_text) {

    override fun createViewHolder(itemView: View): AdapterDelegateViewHolder<ChatEntry> {
        return AgentTextItemViewHolder(
            itemView,
            lifecycleOwner,
            viewModelProvider.get(),
            jivoChatViewModel,
            markwonProvider.get()
        )
    }
}