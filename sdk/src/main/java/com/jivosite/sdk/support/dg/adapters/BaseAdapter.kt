package com.jivosite.sdk.support.dg.adapters

import com.jivosite.sdk.support.dg.AdapterDelegateItem

/**
 * Created on 06/05/2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
interface BaseAdapter<T> {

	var items: List<AdapterDelegateItem<T>>

}