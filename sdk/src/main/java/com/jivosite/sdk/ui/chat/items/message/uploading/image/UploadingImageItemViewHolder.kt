package com.jivosite.sdk.ui.chat.items.message.uploading.image

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.databinding.DgItemUploadingImageBinding
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.UploadingFileEntry
import com.jivosite.sdk.ui.chat.items.message.general.ChatEntryItemViewHolder

/**
 * Created on 2/24/21.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class UploadingImageItemViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    private val viewModel: UploadingImageItemViewModel
) : ChatEntryItemViewHolder<UploadingFileEntry>(itemView) {

    init {
        val binding = DgItemUploadingImageBinding.bind(itemView)
        binding.view = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = lifecycleOwner
    }

    override fun bind(item: AdapterDelegateItem<ChatEntry>) {
        super.bind(item)
        val data = item.requireData()
        if (data is UploadingFileEntry) {
            viewModel.entry = data
        }
    }
}