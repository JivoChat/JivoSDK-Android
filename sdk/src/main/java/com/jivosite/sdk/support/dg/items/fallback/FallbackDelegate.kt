package com.jivosite.sdk.support.dg.items.fallback

import android.view.View
import com.jivosite.sdk.R

import com.jivosite.sdk.support.dg.AdapterDelegate
import com.jivosite.sdk.support.dg.AdapterDelegateItem.Companion.VT_FALLBACK

/**
 * Created on 2020-01-28.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class FallbackDelegate<T> : AdapterDelegate<T>(VT_FALLBACK, R.layout.dg_item_fallback) {

    override fun createViewHolder(itemView: View) = FallbackViewHolder<T>(itemView)
}