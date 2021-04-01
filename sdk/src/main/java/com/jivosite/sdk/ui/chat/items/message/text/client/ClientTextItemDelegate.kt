package com.jivosite.sdk.ui.chat.items.message.text.client

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.R
import com.jivosite.sdk.support.dg.AdapterDelegate
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.ChatItem.Companion.VT_CLIENT_TEXT
import javax.inject.Provider

/**
 * Created on 16.09.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class ClientTextItemDelegate(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModelProvider: Provider<ClientTextItemViewModel>
) : AdapterDelegate<ChatEntry>(VT_CLIENT_TEXT, R.layout.dg_item_client_text) {

    override fun createViewHolder(itemView: View): AdapterDelegateViewHolder<ChatEntry> {
        return ClientTextItemViewHolder(itemView, lifecycleOwner, viewModelProvider.get())
    }
}