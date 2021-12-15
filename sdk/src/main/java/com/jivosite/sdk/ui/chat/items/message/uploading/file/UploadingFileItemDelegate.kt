package com.jivosite.sdk.ui.chat.items.message.uploading.file

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.jivosite.sdk.R
import com.jivosite.sdk.support.dg.AdapterDelegate
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder
import com.jivosite.sdk.ui.chat.items.ChatEntry
import com.jivosite.sdk.ui.chat.items.ChatItem
import javax.inject.Provider

/**
 * Created on 2/25/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class UploadingFileItemDelegate(
    private val lifecycleOwner: LifecycleOwner,
    private val viewModelProvider: Provider<UploadingFileItemViewModel>
) : AdapterDelegate<ChatEntry>(ChatItem.VT_UPLOADING_FILE, R.layout.dg_item_uploading_file) {

    override fun createViewHolder(itemView: View): AdapterDelegateViewHolder<ChatEntry> {
        return UploadingFileItemViewHolder(itemView, lifecycleOwner, viewModelProvider.get())
    }
}