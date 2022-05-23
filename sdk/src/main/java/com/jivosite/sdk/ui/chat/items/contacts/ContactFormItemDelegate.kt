package com.jivosite.sdk.ui.chat.items.contacts

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.R
import com.jivosite.sdk.support.dg.AdapterDelegate
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.ChatItem
import javax.inject.Provider

/**
 * Created on 14.04.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class ContactFormItemDelegate(
    private val lifecycleOwner: LifecycleOwner,
    private val itemViewModelProvider: Provider<ContactFormItemViewModel>
) : AdapterDelegate<ChatEntry>(ChatItem.VT_CONTACT_FORM, R.layout.dg_item_contact_form) {

    override fun createViewHolder(itemView: View): AdapterDelegateViewHolder<ChatEntry> {
        return ContactFormItemViewHolder(itemView, lifecycleOwner, itemViewModelProvider.get())
    }
}