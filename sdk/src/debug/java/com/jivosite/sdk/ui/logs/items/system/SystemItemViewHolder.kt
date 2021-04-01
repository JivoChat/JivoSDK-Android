package com.jivosite.sdk.ui.logs.items.system

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.databinding.DgItemLogsSystemBinding
import com.jivosite.sdk.ui.logs.items.LogsItemViewHolder

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class SystemItemViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    private val viewModel: SystemItemViewModel
) : LogsItemViewHolder(itemView, viewModel) {

    init {
        DgItemLogsSystemBinding.bind(itemView).also {
            it.viewModel = viewModel
            it.lifecycleOwner = lifecycleOwner
        }
    }
}