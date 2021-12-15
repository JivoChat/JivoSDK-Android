package com.jivosite.sdk.support.dg

/**
 * Created on 2020-01-28.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
abstract class AdapterDelegateItem<T>(val viewType: Int = VT_ITEM, val data: T? = null) {

    companion object {
        const val VT_FALLBACK = 0
        const val VT_EMPTY = 1
        const val VT_LOADING = 2
        const val VT_ERROR = 3
        const val VT_ITEM = 4
    }

    open fun requireData(): T = data
        ?: throw IllegalStateException("There is no data in ${javaClass.simpleName}")

}