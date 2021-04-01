package com.jivosite.sdk.support.dg

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Created on 2020-01-28.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
abstract class AdapterDelegateAdapter<T> : RecyclerView.Adapter<AdapterDelegateViewHolder<T>>() {

    private val delegatesManager = AdapterDelegateManager<T>()

    fun add(delegate: AdapterDelegate<T>) = delegatesManager.add(delegate)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterDelegateViewHolder<T> {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: AdapterDelegateViewHolder<T>, position: Int) {
        delegatesManager.onBindViewHolder(holder, getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    abstract fun getItem(position: Int): AdapterDelegateItem<T>
}