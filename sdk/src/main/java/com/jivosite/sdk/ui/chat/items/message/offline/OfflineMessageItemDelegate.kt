package com.jivosite.sdk.ui.chat.items.message.offlineimport android.view.Viewimport com.jivosite.sdk.Rimport com.jivosite.sdk.support.dg.AdapterDelegateimport com.jivosite.sdk.support.dg.AdapterDelegateViewHolderimport com.jivosite.sdk.ui.chat.items.ChatEntryimport com.jivosite.sdk.ui.chat.items.ChatItem.Companion.VT_OFFLINE/** * Created on 22.02.2022. * * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com) */class OfflineMessageItemDelegate : AdapterDelegate<ChatEntry>(VT_OFFLINE, R.layout.dg_item_offline_message) {    override fun createViewHolder(itemView: View): AdapterDelegateViewHolder<ChatEntry> {        return OfflineMessageItemViewHolder(itemView)    }}