package com.jivosite.sdk.ui.logs.items.error

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.databinding.DgItemLogsErrorBinding
import com.jivosite.sdk.ui.logs.items.LogsItemViewHolder

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class ErrorItemViewHolder(
    itemView: View,
    lifecycleOwner: LifecycleOwner,
    viewModel: ErrorItemViewModel
) : LogsItemViewHolder(itemView, viewModel) {

    init {
        DgItemLogsErrorBinding.bind(itemView).also {
            it.viewModel = viewModel
            it.lifecycleOwner = lifecycleOwner
        }
    }
}