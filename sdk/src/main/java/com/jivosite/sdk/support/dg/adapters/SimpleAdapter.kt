package com.jivosite.sdk.support.dg.adapters

import com.jivosite.sdk.support.dg.AdapterDelegateAdapter
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import java.util.*

/**
 * Created on 2020-01-28.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class SimpleAdapter<T> : AdapterDelegateAdapter<T>(), BaseAdapter<T> {

    override var items: List<AdapterDelegateItem<T>> = Collections.emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItem(position: Int) = items[position]

    override fun getItemCount() = items.size
}