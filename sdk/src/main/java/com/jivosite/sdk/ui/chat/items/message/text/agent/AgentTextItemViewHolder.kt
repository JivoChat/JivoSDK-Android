package com.jivosite.sdk.ui.chat.items.message.text.agent

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.databinding.DgItemAgentTextBinding
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.ui.chat.items.AgentMessageEntry
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.message.general.MessageItemViewHolder

/**
 * Created on 16.09.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class AgentTextItemViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    private val viewModel: AgentTextItemViewModel
) : MessageItemViewHolder<AgentMessageEntry>(itemView, viewModel) {

    init {
        val binding = DgItemAgentTextBinding.bind(itemView)
        binding.viewModel = viewModel
        binding.lifecycleOwner = lifecycleOwner

        itemView.setOnLongClickListener {
            copyToClipboard(viewModel.text.value)
            true
        }
    }

    override fun bind(item: AdapterDelegateItem<ChatEntry>) {
        super.bind(item)
        val data = item.requireData()
        if (data is AgentMessageEntry) {
            viewModel.entry = data
        }
    }
}