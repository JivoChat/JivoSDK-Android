package com.jivosite.sdk.ui.chat.items.message.uploading.image

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.R
import com.jivosite.sdk.support.dg.AdapterDelegate
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.ChatItem
import javax.inject.Provider

/**
 * Created on 2/24/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class UploadingImageItemDelegate(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModelProvider: Provider<UploadingImageItemViewModel>
) : AdapterDelegate<ChatEntry>(ChatItem.VT_UPLOADING_IMAGE, R.layout.dg_item_uploading_image) {

    override fun createViewHolder(itemView: View): AdapterDelegateViewHolder<ChatEntry> {
        return UploadingImageItemViewHolder(itemView, lifecycleOwner, viewModelProvider.get())
    }
}