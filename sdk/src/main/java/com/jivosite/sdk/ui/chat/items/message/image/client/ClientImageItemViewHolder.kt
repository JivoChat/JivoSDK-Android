package com.jivosite.sdk.ui.chat.items.message.image.client

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.databinding.DgItemClientImageBinding
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.ClientMessageEntry
import com.jivosite.sdk.ui.chat.items.message.media.MediaItemViewHolder

/**
 * Created on 2/16/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class ClientImageItemViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    private val viewModel: ClientImageItemViewModel
) : MediaItemViewHolder<ClientMessageEntry>(itemView, viewModel) {

    init {
        val binding = DgItemClientImageBinding.bind(itemView)
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
}