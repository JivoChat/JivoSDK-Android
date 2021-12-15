package com.jivosite.sdk.support.dg.adapters

import androidx.recyclerview.widget.DiffUtil
import com.jivosite.sdk.support.dg.AdapterDelegateAdapter
import com.jivosite.sdk.support.dg.AdapterDelegateItem
import java.util.*

/**
 * Created on 2020-02-06.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
open class SimpleDiffAdapter<T>(
	private val comparator: SimpleDiffComparator<AdapterDelegateItem<T>> = DefaultComparator()
) : AdapterDelegateAdapter<T>(), BaseAdapter<T> {

    override var items: List<AdapterDelegateItem<T>> = Collections.emptyList()
        set(value) {
            if (field.isEmpty()) {
                field = value
                notifyDataSetChanged()
            } else {
                val diffCallback = SimpleDiffCallback(value, field, comparator)
                val result = DiffUtil.calculateDiff(diffCallback)
                field = value
                result.dispatchUpdatesTo(this)
            }
        }

    override fun getItem(position: Int): AdapterDelegateItem<T> = items[position]

    override fun getItemCount() = items.size

    private class SimpleDiffCallback<T>(
		private val newList: List<AdapterDelegateItem<T>>,
		private val oldList: List<AdapterDelegateItem<T>>,
		private val comparator: SimpleDiffComparator<AdapterDelegateItem<T>>
	) : DiffUtil.Callback() {

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return comparator.sameItems(oldList[oldItemPosition], newList[newItemPosition])
        }

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return comparator.sameContent(oldList[oldItemPosition], newList[newItemPosition])
        }
    }

    private class DefaultComparator<T> : SimpleDiffComparator<AdapterDelegateItem<T>> {

        override fun sameItems(
			left: AdapterDelegateItem<T>,
			right: AdapterDelegateItem<T>
		): Boolean {
            return left.viewType == right.viewType
        }

        override fun sameContent(
			left: AdapterDelegateItem<T>,
			right: AdapterDelegateItem<T>
		): Boolean {
            return left.data == right.data
        }
    }
}