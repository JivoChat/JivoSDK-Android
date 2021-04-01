package com.jivosite.sdk.ui.logs.items.message

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.R
import com.jivosite.sdk.logger.LogMessage
import com.jivosite.sdk.support.dg.AdapterDelegate
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.logs.JivoLogsViewModel
import com.jivosite.sdk.ui.logs.items.LogsItem.Companion.VT_MESSAGE
import javax.inject.Provider

/**
 * Created on 12/8/20.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class MessageItemDelegate(
    private val lifecycleOwner: LifecycleOwner,
    private val logsViewModel: JivoLogsViewModel,
    private val viewModelProvider: Provider<MessageItemViewModel>
) : AdapterDelegate<LogMessage>(VT_MESSAGE, R.layout.dg_item_logs_message) {

    override fun createViewHolder(itemView: View): AdapterDelegateViewHolder<LogMessage> {
        return MessageItemViewHolder(itemView, lifecycleOwner, logsViewModel, viewModelProvider.get())
    }
}