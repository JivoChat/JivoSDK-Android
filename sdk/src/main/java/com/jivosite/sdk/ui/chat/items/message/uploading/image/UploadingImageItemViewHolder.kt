package com.jivosite.sdk.ui.chat.items.message.uploading.image

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.Jivo
import com.jivosite.sdk.R
import com.jivosite.sdk.databinding.DgItemUploadingImageBinding
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.UploadingFileEntry
import com.jivosite.sdk.ui.chat.items.message.general.MessageItemViewHolder

/**
 * Created on 2/24/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class UploadingImageItemViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    private val viewModel: UploadingImageItemViewModel
) : MessageItemViewHolder<UploadingFileEntry>(itemView, viewModel) {

    init {
        val binding = DgItemUploadingImageBinding.bind(itemView)
        binding.view = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = lifecycleOwner

        val background = AppCompatResources.getDrawable(context, R.drawable.bg_outgoing_message)
        val color = AppCompatResources.getColorStateList(context, Jivo.getConfig().outgoingMessageColor.color)
        background?.setTintList(color)
        binding.progress.indeterminateDrawable.colorFilter = PorterDuffColorFilter(color.defaultColor, PorterDuff.Mode.SRC_IN)
    }

    override fun bind(item: AdapterDelegateItem<ChatEntry>) {
        super.bind(item)
        val data = item.requireData()
        if (data is UploadingFileEntry) {
            viewModel.entry = data
        }
    }
}
