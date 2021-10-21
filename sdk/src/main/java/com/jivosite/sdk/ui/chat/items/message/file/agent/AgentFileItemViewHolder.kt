package com.jivosite.sdk.ui.chat.items.message.file.agent

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.databinding.DgItemAgentFileBinding
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.support.ext.Intents
import com.jivosite.sdk.ui.chat.items.AgentMessageEntry
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.message.general.MessageItemViewHolder

/**
 * Created on 3/2/21.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class AgentFileItemViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    private val viewModel: AgentFileItemViewModel
) : MessageItemViewHolder<AgentMessageEntry>(itemView, viewModel) {

    init {
        val binding = DgItemAgentFileBinding.bind(itemView)
        binding.view = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = lifecycleOwner

        viewModel.state.observe(lifecycleOwner) {
            if (it.hasCompletedCheck && !it.media.isExpired) {
                Intents.downloadFile(context, it.media.path)
            }
        }
    }

    override fun bind(item: AdapterDelegateItem<ChatEntry>) {
        super.bind(item)
        val data = item.requireData()
        if (data is AgentMessageEntry) {
            viewModel.entry = data
        }
    }

    fun onDownload() {
        viewModel.checkLink()
    }
}
