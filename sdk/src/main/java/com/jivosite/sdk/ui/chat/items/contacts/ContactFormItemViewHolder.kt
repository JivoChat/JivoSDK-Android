package com.jivosite.sdk.ui.chat.items.contacts

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.R
import com.jivosite.sdk.databinding.DgItemContactFormBinding
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.ContactFormEntry

/**
 * Created on 14.04.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class ContactFormItemViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    private val viewModel: ContactFormItemViewModel
) : AdapterDelegateViewHolder<ChatEntry>(itemView) {

    init {
        val binding = DgItemContactFormBinding.bind(itemView)
        binding.viewModel = viewModel
        binding.lifecycleOwner = lifecycleOwner

        viewModel.hasSentContacts.observe(lifecycleOwner) {
            binding.sendUserInfo.text = context.getString(if (it) R.string.contact_form_status_sent else R.string.common_send)
        }
        viewModel.hasAgentsOnline.observe(lifecycleOwner) {
            binding.contactFormPlaceholder.text =
                context.getString(if (it) R.string.chat_system_contact_form_introduce_in_chat else R.string.chat_system_contact_form_must_fill)
        }
    }

    override fun bind(item: AdapterDelegateItem<ChatEntry>) {
        super.bind(item)
        val data = item.requireData()
        if (data is ContactFormEntry) {
            viewModel.entry = data
        }
    }
}