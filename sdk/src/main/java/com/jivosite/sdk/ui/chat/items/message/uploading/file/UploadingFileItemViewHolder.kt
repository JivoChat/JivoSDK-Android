package com.jivosite.sdk.ui.chat.items.message.uploading.file

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.databinding.DgItemUploadingFileBinding
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.UploadingFileEntry
import com.jivosite.sdk.ui.chat.items.message.general.MessageItemViewHolder

/**
 * Created on 2/25/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class UploadingFileItemViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    private val viewModel: UploadingFileItemViewModel
) : MessageItemViewHolder<UploadingFileEntry>(itemView, viewModel) {

    init {
        val binding = DgItemUploadingFileBinding.bind(itemView)
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
