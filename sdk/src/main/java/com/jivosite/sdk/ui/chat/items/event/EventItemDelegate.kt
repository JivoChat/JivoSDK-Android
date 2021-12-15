package com.jivosite.sdk.ui.chat.items.event

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.R
import com.jivosite.sdk.support.dg.AdapterDelegate
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.ChatItem
import javax.inject.Provider

/**
 * Created on 2/9/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class EventItemDelegate(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModelProvider: Provider<EventItemViewModel>
) : AdapterDelegate<ChatEntry>(ChatItem.VT_EVENT, R.layout.dg_item_event) {

    override fun createViewHolder(itemView: View): AdapterDelegateViewHolder<ChatEntry> {
        return EventItemViewHolder(itemView, lifecycleOwner, viewModelProvider.get())
    }
}