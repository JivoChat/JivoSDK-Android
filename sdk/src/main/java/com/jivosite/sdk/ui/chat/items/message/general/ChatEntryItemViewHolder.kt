package com.jivosite.sdk.ui.chat.items.message.general

import android.view.View
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.chat.items.ChatEntry

/**
 * Created on 2/24/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
open class ChatEntryItemViewHolder<T : ChatEntry>(
    itemView: View
) : AdapterDelegateViewHolder<ChatEntry>(itemView)