package com.jivosite.sdk.ui.logs.items

import android.view.View
import com.jivosite.sdk.logger.LogMessage
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
open class LogsItemViewHolder(
    itemView: View,
    private val viewModel: LogsItemViewModel
) : AdapterDelegateViewHolder<LogMessage>(itemView) {

    override fun bind(item: AdapterDelegateItem<LogMessage>) {
        super.bind(item)
        viewModel.message = item.requireData()
    }
}