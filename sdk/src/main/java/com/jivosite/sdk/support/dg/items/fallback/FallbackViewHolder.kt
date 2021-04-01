package com.jivosite.sdk.support.dg.items.fallback

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import com.jivosite.sdk.R
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import com.jivosite.sdk.support.dg.AdapterDelegateViewHolder

/**
 * Created on 2020-01-28.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class FallbackViewHolder<T>(itemView: View) : AdapterDelegateViewHolder<T>(itemView) {

    private val titleView: TextView by lazy {
        itemView.findViewById(R.id.title)
    }

    @SuppressLint("SetTextI18n")
    override fun bind(item: AdapterDelegateItem<T>) {
        super.bind(item)
        titleView.text = "View type ${item.viewType}"
    }
}