package com.jivosite.sdk.ui.chat.items.message.image.agent

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.databinding.DgItemAgentImageBinding
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.support.ext.Intents
import com.jivosite.sdk.ui.chat.items.AgentMessageEntry
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.message.general.MessageItemViewHolder

/**
 * Created on 16.09.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class AgentImageItemViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    private val viewModel: AgentImageItemViewModel
) : MessageItemViewHolder<AgentMessageEntry>(itemView, viewModel) {

    init {
        val binding = DgItemAgentImageBinding.bind(itemView)
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

    fun onClick() {
        viewModel.imageUrl.value?.let { url ->
            Intents.showActivityImageViewer(context, url, viewModel.imageName)
        }
    }
}