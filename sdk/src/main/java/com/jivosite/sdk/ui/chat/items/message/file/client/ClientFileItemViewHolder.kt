package com.jivosite.sdk.ui.chat.items.message.file.client

import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.R
import com.jivosite.sdk.databinding.DgItemClientFileBinding
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.ClientMessageEntry
import com.jivosite.sdk.ui.chat.items.message.media.MediaItemViewHolder

/**
 * Created on 3/2/21.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class ClientFileItemViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    private val viewModel: ClientFileItemViewModel
) : MediaItemViewHolder<ClientMessageEntry>(itemView, viewModel) {

    init {
        val binding = DgItemClientFileBinding.bind(itemView)
        binding.view = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = lifecycleOwner

        val background = AppCompatResources.getDrawable(context, R.drawable.bg_outgoing_message)
        val color =
            AppCompatResources.getColorStateList(context, Jivo.getConfig().outgoingMessageColor.color)
        background?.setTintList(color)
        binding.bubble.background = background
        binding.picture.imageTintList =
            AppCompatResources.getColorStateList(context, Jivo.getConfig().outgoingMessageColor.color)
    }

    override fun bind(item: AdapterDelegateItem<ChatEntry>) {
        super.bind(item)
        val data = item.requireData()
        if (data is ClientMessageEntry) {
            viewModel.entry = data
        }
    }
}