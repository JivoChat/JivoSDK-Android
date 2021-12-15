package com.jivosite.sdk.ui.chat.items.message.file.client

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
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class ClientFileItemDelegate(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModelProvider: Provider<ClientFileItemViewModel>
) : AdapterDelegate<ChatEntry>(ChatItem.VT_CLIENT_FILE_ITEM, R.layout.dg_item_client_file) {

    override fun createViewHolder(itemView: View): AdapterDelegateViewHolder<ChatEntry> {
        return ClientFileItemViewHolder(itemView, lifecycleOwner, viewModelProvider.get())
    }
}