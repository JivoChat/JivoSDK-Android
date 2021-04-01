package com.jivosite.sdk.ui.chat.items.event

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.databinding.DgItemEventBinding
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.EventEntry

/**
 * Created on 2/9/21.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class EventItemViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    private val viewModel: EventItemViewModel
) : AdapterDelegateViewHolder<ChatEntry>(itemView) {

    init {
        val binding = DgItemEventBinding.bind(itemView)
        binding.viewModel = viewModel
        binding.lifecycleOwner = lifecycleOwner
    }

    override fun bind(item: AdapterDelegateItem<ChatEntry>) {
        super.bind(item)
        val data = item.requireData()
        if (data is EventEntry) {
            viewModel.event = data
        }
    }
}