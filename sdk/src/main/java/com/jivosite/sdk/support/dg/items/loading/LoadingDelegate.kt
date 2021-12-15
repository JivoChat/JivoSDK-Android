package com.jivosite.sdk.support.dg.items.loading

import android.view.View
import com.jivosite.sdk.R
import com.jivosite.sdk.support.dg.AdapterDelegate
import com.jivosite.sdk.support.dg.AdapterDelegateItem.Companion.VT_LOADING

/**
 * Created on 2020-01-28.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class LoadingDelegate<T> : AdapterDelegate<T>(VT_LOADING, R.layout.dg_item_loading) {

    override fun createViewHolder(itemView: View) = LoadingViewHolder<T>(itemView)
}