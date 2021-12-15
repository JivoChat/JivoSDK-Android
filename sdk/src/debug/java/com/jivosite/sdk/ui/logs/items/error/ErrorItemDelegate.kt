package com.jivosite.sdk.ui.logs.items.error

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.R
import com.jivosite.sdk.logger.LogMessage
import com.jivosite.sdk.support.dg.AdapterDelegate
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.logs.items.LogsItem.Companion.VT_ERROR
import javax.inject.Provider

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class ErrorItemDelegate(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModelProvider: Provider<ErrorItemViewModel>
) : AdapterDelegate<LogMessage>(VT_ERROR, R.layout.dg_item_logs_error) {

    override fun createViewHolder(itemView: View): AdapterDelegateViewHolder<LogMessage> {
        return ErrorItemViewHolder(itemView, lifecycleOwner, viewModelProvider.get())
    }
}