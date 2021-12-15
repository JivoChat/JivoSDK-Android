package com.jivosite.sdk.ui.logs.items.system

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.R
import com.jivosite.sdk.logger.LogMessage
import com.jivosite.sdk.support.dg.AdapterDelegate
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.logs.items.LogsItem.Companion.VT_SYSTEM
import javax.inject.Provider

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class SystemItemDelegate(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModelProvider: Provider<SystemItemViewModel>
) : AdapterDelegate<LogMessage>(VT_SYSTEM, R.layout.dg_item_logs_system) {

    override fun createViewHolder(itemView: View): AdapterDelegateViewHolder<LogMessage> {
        return SystemItemViewHolder(itemView, lifecycleOwner, viewModelProvider.get())
    }
}