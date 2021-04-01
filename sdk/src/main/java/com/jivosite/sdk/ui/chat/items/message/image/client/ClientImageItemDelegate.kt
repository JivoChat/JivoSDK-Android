package com.jivosite.sdk.ui.chat.items.message.image.client

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.R
import com.jivosite.sdk.support.dg.AdapterDelegate
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.ChatItem
import javax.inject.Provider

/**
 * Created on 2/16/21.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class ClientImageItemDelegate(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModelProvider: Provider<ClientImageItemViewModel>
) : AdapterDelegate<ChatEntry>(ChatItem.VT_CLIENT_IMAGE, R.layout.dg_item_client_image) {

    override fun createViewHolder(itemView: View): AdapterDelegateViewHolder<ChatEntry> {
        return ClientImageItemViewHolder(itemView, lifecycleOwner, viewModelProvider.get())
    }
}