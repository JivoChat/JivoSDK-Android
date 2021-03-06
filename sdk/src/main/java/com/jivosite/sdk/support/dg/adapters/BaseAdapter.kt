package com.jivosite.sdk.support.dg.adapters

import com.jivosite.sdk.support.dg.AdapterDelegateItem

/**
 * Created on 06/05/2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
interface BaseAdapter<T> {

	var items: List<AdapterDelegateItem<T>>

}