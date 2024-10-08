package com.jivosite.sdk.support.utils

import android.content.Context
import com.jivosite.sdk.R
import kotlin.math.pow

/**
 * Created on 18.04.2024.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */

fun getFileSize(context: Context, size: Long) = when (size) {
    in 0..999 -> context.getString(R.string.format_file_size_b, size.toDouble())
    in 1000..999999 -> context.getString(R.string.format_file_size_kb, size.div(1000.0))
    else -> context.getString(R.string.format_file_size_mb, size.div(10.0.pow(6)))
}

