package com.jivosite.sdk.support.dg

import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import com.jivosite.sdk.support.dg.AdapterDelegateItem.Companion.VT_FALLBACK
import com.jivosite.sdk.support.dg.items.fallback.FallbackDelegate

/**
 * Created on 2020-01-28.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class AdapterDelegateManager<T> {

    private val delegates = SparseArrayCompat<AdapterDelegate<T>>()
    private val fallbackDelegate = FallbackDelegate<T>()

    init {
        add(fallbackDelegate)
    }

    fun <D : AdapterDelegate<T>> add(delegate: D) {
        val d = delegates[delegate.viewType]
        require(d == null) {
            "The delegate has been already added: ${delegate.javaClass.simpleName}"
        }
        delegates.put(delegate.viewType, delegate)
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterDelegateViewHolder<T> {
        return requireDelegate(viewType).onCreateViewHolder(parent)
    }

    fun onBindViewHolder(holder: AdapterDelegateViewHolder<T>, item: AdapterDelegateItem<T>) {
        requireDelegate(holder.itemViewType).onBindViewHolder(holder, item)
    }

    private fun requireDelegate(viewType: Int): AdapterDelegate<T> {
        val delegate = delegates[viewType] ?: delegates[VT_FALLBACK]
        return delegate ?: throw IllegalStateException("Where is fallback delegate?")
    }
}