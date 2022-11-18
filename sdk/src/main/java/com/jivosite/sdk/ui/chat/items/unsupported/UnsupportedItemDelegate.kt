package com.jivosite.sdk.ui.chat.items.unsupported

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.R
import com.jivosite.sdk.support.dg.AdapterDelegate
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.ChatItem
import javax.inject.Provider

/**
 * Created on 10.11.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class UnsupportedItemDelegate(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModelProvider: Provider<UnsupportedItemViewModel>
) : AdapterDelegate<ChatEntry>(ChatItem.VT_UNSUPPORTED, R.layout.dg_item_unsupported) {

    override fun createViewHolder(itemView: View): AdapterDelegateViewHolder<ChatEntry> {
        return UnsupportedItemViewHolder(itemView, lifecycleOwner, viewModelProvider.get())
    }
}