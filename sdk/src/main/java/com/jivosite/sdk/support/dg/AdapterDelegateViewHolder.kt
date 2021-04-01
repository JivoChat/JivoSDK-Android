package com.jivosite.sdk.support.dg

import android.content.Context
import android.view.View
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView

/**
 * Created on 2020-01-28.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
abstract class AdapterDelegateViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val context: Context
        get() = itemView.context

    open val hasDivider: Boolean
        get() = false

    open val hasBottomDivider: Boolean
        get() = false

    var item: AdapterDelegateItem<T>? = null

    @CallSuper
    open fun bind(item: AdapterDelegateItem<T>) {
        this.item = item
    }
}