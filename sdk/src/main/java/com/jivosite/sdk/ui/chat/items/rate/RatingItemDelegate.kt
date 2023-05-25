package com.jivosite.sdk.ui.chat.items.rate

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.R
import com.jivosite.sdk.support.dg.AdapterDelegate
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.ChatItem
import javax.inject.Provider

/**
 * Created on 20.01.2023.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class RatingItemDelegate(
    private val lifecycleOwner: LifecycleOwner,
    private val itemViewModelProvider: Provider<RateItemViewModel>
) : AdapterDelegate<ChatEntry>(ChatItem.VT_RATING, R.layout.dg_item_rating) {

    override fun createViewHolder(itemView: View): AdapterDelegateViewHolder<ChatEntry> {
        return RatingItemViewHolder(itemView, lifecycleOwner, itemViewModelProvider.get())
    }
}
