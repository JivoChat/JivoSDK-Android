package com.jivosite.sdk.ui.chat.items.message.text.agent

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.databinding.DgItemAgentTextBinding
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.ui.chat.items.AgentMessageEntry
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.message.general.MessageItemViewHolder
import io.noties.markwon.Markwon

/**
 * Created on 16.09.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class AgentTextItemViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    private val viewModel: AgentTextItemViewModel,
    private val markwon: Markwon
) : MessageItemViewHolder<AgentMessageEntry>(itemView, viewModel) {

    init {
        val binding = DgItemAgentTextBinding.bind(itemView)
        binding.viewModel = viewModel
        binding.lifecycleOwner = lifecycleOwner

        itemView.setOnLongClickListener {
            copyToClipboard(viewModel.text.value)
            true
        }
        viewModel.text.observe(lifecycleOwner) { message ->
            markwon.setMarkdown(binding.message, message)
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