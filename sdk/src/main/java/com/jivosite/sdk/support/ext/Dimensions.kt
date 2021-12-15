package com.jivosite.sdk.support.ext

import android.content.res.Resources
import android.util.TypedValue

/**
 * Created on 22.09.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
val Int.dp: Int
    get() = this.toFloat().dp.toInt()

val Float.dp: Float
    get() {
        val resources = Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources)
    }