package com.jivosite.sdk.ui.chat.items.unsupported

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.databinding.DgItemUnsupportedBinding
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.UnsupportedEntry

/**
 * Created on 10.11.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class UnsupportedItemViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    private val viewModel: UnsupportedItemViewModel
) : AdapterDelegateViewHolder<ChatEntry>(itemView) {

    init {
        val binding = DgItemUnsupportedBinding.bind(itemView)
        binding.viewModel = viewModel
        binding.lifecycleOwner = lifecycleOwner
    }

    override fun bind(item: AdapterDelegateItem<ChatEntry>) {
        super.bind(item)
        val data = item.requireData()
        if (data is UnsupportedEntry) {
            viewModel.data = data
        }
    }
}