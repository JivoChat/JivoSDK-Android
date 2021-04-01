package com.jivosite.sdk.ui.logs.items.message

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.databinding.DgItemLogsMessageBinding
import com.jivosite.sdk.ui.logs.JivoLogsViewModel
import com.jivosite.sdk.ui.logs.items.LogsItemViewHolder

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class MessageItemViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    private val logsViewModel: JivoLogsViewModel,
    private val itemViewModel: MessageItemViewModel
) : LogsItemViewHolder(itemView, itemViewModel) {

    init {
        DgItemLogsMessageBinding.bind(itemView).also {
            it.viewModel = itemViewModel
            it.lifecycleOwner = lifecycleOwner
        }

        itemView.setOnLongClickListener {
            logsViewModel.messageToSend.value = itemViewModel.text.value
            true
        }
    }
}