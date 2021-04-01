package com.jivosite.sdk.support.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

/**
 * Created on 12/9/20.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
fun String?.copyToClipboard(context: Context): Boolean {
    val text = this ?: return false
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager? ?: return false
    clipboardManager.setPrimaryClip(ClipData.newPlainText("text", text))
    return true
}