package com.jivosite.sdk.ui.chat.items.message.text.client

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.R
import com.jivosite.sdk.databinding.DgItemClientTextBinding
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.MessageEntry
import com.jivosite.sdk.ui.chat.items.message.general.MessageItemViewHolder

/**
 * Created on 16.09.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class ClientTextItemViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    private val viewModel: ClientTextItemViewModel
) : MessageItemViewHolder<MessageEntry>(itemView, viewModel) {

    init {
        val binding = DgItemClientTextBinding.bind(itemView)
        binding.viewModel = viewModel
        binding.lifecycleOwner = lifecycleOwner

        val background = AppCompatResources.getDrawable(context, R.drawable.bg_outgoing_message)
        val color =
            AppCompatResources.getColorStateList(context, Jivo.getConfig().outgoingMessageColor.color)
        background?.setTintList(color)
        binding.bubble.background = background

        itemView.setOnLongClickListener {
            copyToClipboard(viewModel.text.value)
            true
        }
    }

    override fun bind(item: AdapterDelegateItem<ChatEntry>) {
        super.bind(item)
        val data = item.requireData()
        if (data is MessageEntry) {
            viewModel.entry = data
        }
    }
}