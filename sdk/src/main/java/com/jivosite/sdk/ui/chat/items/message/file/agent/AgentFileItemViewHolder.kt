package com.jivosite.sdk.ui.chat.items.message.file.agent

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.databinding.DgItemAgentFileBinding
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.ui.chat.items.AgentMessageEntry
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.message.media.MediaItemViewHolder

/**
 * Created on 3/2/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class AgentFileItemViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    private val viewModel: AgentFileItemViewModel
) : MediaItemViewHolder<AgentMessageEntry>(itemView, viewModel) {

    init {
        val binding = DgItemAgentFileBinding.bind(itemView)
        binding.view = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = lifecycleOwner

    }

    override fun bind(item: AdapterDelegateItem<ChatEntry>) {
        super.bind(item)
        val data = item.requireData()
        if (data is AgentMessageEntry) {
            viewModel.entry = data
        }
    }
}
