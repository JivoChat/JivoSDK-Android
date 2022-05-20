package com.jivosite.sdk.ui.chat

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jivosite.sdk.support.ext.dp
import com.jivosite.sdk.ui.chat.items.EntryPosition
import com.jivosite.sdk.ui.chat.items.contacts.ContactFormItemViewHolder
import com.jivosite.sdk.ui.chat.items.message.general.MessageItemViewHolder

/**
 * Created on 29.09.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class JivoChatDecoration : RecyclerView.ItemDecoration() {

    private val paddingTopBig: Int = 16.dp
    private val paddingTopSmall: Int = 4.dp

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (RecyclerView.NO_POSITION != parent.getChildAdapterPosition(view)) {
            when (val holder = parent.getChildViewHolder(view)) {
                is MessageItemViewHolder<*> -> {
                    when (holder.getEntryPosition()) {
                        EntryPosition.Single, EntryPosition.Last -> outRect.top = paddingTopBig
                        else -> outRect.top = paddingTopSmall
                    }
                }
                is ContactFormItemViewHolder -> {
                    outRect.top = paddingTopBig
                }
            }
        }
    }
}