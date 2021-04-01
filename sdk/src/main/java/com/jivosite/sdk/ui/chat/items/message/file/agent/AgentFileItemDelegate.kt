package com.jivosite.sdk.ui.chat.items.message.file.agent

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.R
import com.jivosite.sdk.support.dg.AdapterDelegate
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.ChatItem
import javax.inject.Provider

/**
 * Created on 3/2/21.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class AgentFileItemDelegate(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModelProvider: Provider<AgentFileItemViewModel>
) : AdapterDelegate<ChatEntry>(ChatItem.VT_AGENT_FILE_ITEM, R.layout.dg_item_agent_file) {

    override fun createViewHolder(itemView: View): AdapterDelegateViewHolder<ChatEntry> {
        return AgentFileItemViewHolder(itemView, lifecycleOwner, viewModelProvider.get())
    }
}