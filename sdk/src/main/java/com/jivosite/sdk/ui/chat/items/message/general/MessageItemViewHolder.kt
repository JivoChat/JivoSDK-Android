package com.jivosite.sdk.ui.chat.items.message.general

import android.view.View
import android.widget.Toast
import com.jivosite.sdk.R
import com.jivosite.sdk.support.utils.copyToClipboard
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.EntryPosition
import com.jivosite.sdk.ui.chat.items.MessageEntry

/**
 * Created on 16.09.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
open class MessageItemViewHolder<T : MessageEntry>(
    itemView: View,
    private val viewModel: MessageItemViewModel<T>
) : ChatEntryItemViewHolder<ChatEntry>(itemView) {

    fun isLast(): Boolean = viewModel.position == EntryPosition.Last

    fun copyToClipboard(text: String?) {
        if (text.copyToClipboard(context)) {
            Toast.makeText(context, R.string.toast_copied_to_clipboard, Toast.LENGTH_SHORT).show()
        }
    }
}