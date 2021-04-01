package com.jivosite.sdk.ui.chat.items.message.file.client

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.databinding.DgItemClientFileBinding
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.support.ext.Intents
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.ClientMessageEntry
import com.jivosite.sdk.ui.chat.items.message.general.MessageItemViewHolder

/**
 * Created on 3/2/21.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class ClientFileItemViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    private val viewModel: ClientFileItemViewModel
) : MessageItemViewHolder<ClientMessageEntry>(itemView, viewModel) {

    init {
        val binding = DgItemClientFileBinding.bind(itemView)
        binding.view = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = lifecycleOwner
    }

    override fun bind(item: AdapterDelegateItem<ChatEntry>) {
        super.bind(item)
        val data = item.requireData()
        if (data is ClientMessageEntry) {
            viewModel.entry = data
        }
    }

    fun onClick() {
        viewModel.url?.let { url ->
            Intents.downloadFile(context, url)
        }
    }
}