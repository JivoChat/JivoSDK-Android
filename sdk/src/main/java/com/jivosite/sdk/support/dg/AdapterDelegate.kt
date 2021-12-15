package com.jivosite.sdk.support.dg

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

/**
 * Created on 2020-01-28.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
abstract class AdapterDelegate<T>(
        val viewType: Int,
        @LayoutRes private val layoutResId: Int
) {

    fun onCreateViewHolder(parent: ViewGroup): AdapterDelegateViewHolder<T> {
        val inflater = LayoutInflater.from(parent.context)
        val itemView: View = inflater.inflate(layoutResId, parent, false)
        return createViewHolder(itemView)
    }

    open fun onBindViewHolder(holder: AdapterDelegateViewHolder<T>, item: AdapterDelegateItem<T>) {
        holder.bind(item)
    }

    abstract fun createViewHolder(itemView: View): AdapterDelegateViewHolder<T>
}