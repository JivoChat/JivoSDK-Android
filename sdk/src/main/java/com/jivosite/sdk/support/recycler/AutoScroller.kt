package com.jivosite.sdk.support.recycler

import androidx.recyclerview.widget.RecyclerView

/**
 * Created on 2/3/21.
 *
 * Вспомогательный класс, для подскроливания списка к последнему сообщению.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class AutoScroller {

    private var recyclerView: RecyclerView? = null

    private val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            if (positionStart == 0 && recyclerView != null) {
                recyclerView?.scrollToPosition(positionStart)
            }
        }
    }

    fun attachToRecyclerView(recyclerView: RecyclerView?) {
        if (this.recyclerView != null) {
            this.recyclerView?.adapter?.unregisterAdapterDataObserver(observer)
        }

        this.recyclerView = recyclerView
        if (recyclerView != null) {
            recyclerView.adapter?.registerAdapterDataObserver(observer)
        }
    }

    fun release() {
        attachToRecyclerView(null)
    }
}