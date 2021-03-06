package com.jivosite.sdk.support.dg.adapters

/**
 * Created on 11/04/2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
interface SimpleDiffComparator<T> {

    fun sameItems(left: T, right: T): Boolean

    fun sameContent(left: T, right: T): Boolean
}
