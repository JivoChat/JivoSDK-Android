package com.jivosite.sdk.ui.chat.items.message.text.agent

import android.view.View
import androidx.core.view.isEmpty
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.chip.Chip
import com.jivosite.sdk.databinding.DgItemAgentTextBinding
import com.jivosite.sdk.model.pojo.message.ClientMessage
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.ui.chat.JivoChatViewModel
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
    private val jivoChatViewModel: JivoChatViewModel,
    private val markwon: Markwon
) : MessageItemViewHolder<AgentMessageEntry>(itemView, viewModel) {

    companion object {
        private const val DELIMITER = "⦀"
    }

    init {
        val binding = DgItemAgentTextBinding.bind(itemView).also {
            it.view = this
            it.viewModel = viewModel
            it.lifecycleOwner = lifecycleOwner
        }

        viewModel.jivoChatViewModel = jivoChatViewModel

        itemView.setOnLongClickListener {
            copyToClipboard(viewModel.messageEntry.value?.data)
            true
        }

        viewModel.messageEntry.observe(lifecycleOwner) { messageEntry ->
            val message = messageEntry.message.data
            val isLastMessage = messageEntry.isLastMessage

            if (message.contains(DELIMITER)) {
                message.split(DELIMITER).also {
                    markwon.setMarkdown(binding.message, it.first())
                    if (isLastMessage) {
                        binding.buttons.run {
                            removeAllViewsInLayout()
                            isVisible = true
                            if (isEmpty()) {
                                it.forEachIndexed { index, s ->
                                    if (index > 0) {
                                        addView(Chip(context).apply {
                                            text = s
                                            id = index
                                        })
                                    }
                                }
                                setOnCheckedStateChangeListener { _, checkedIds ->
                                    viewModel.sendMessage(ClientMessage.createText(it[checkedIds.first()]))
                                }
                            }
                        }
                    } else {
                        binding.buttons.isVisible = false
                    }
                }
            } else if (messageEntry.from.toLong() < 0) {
                markwon.setMarkdown(binding.message, message)
            } else {
                binding.message.text = message
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
}