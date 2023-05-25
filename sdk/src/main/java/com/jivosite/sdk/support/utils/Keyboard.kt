package com.jivosite.sdk.support.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created on 12/9/20.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
fun String?.copyToClipboard(context: Context): Boolean {
    val text = this ?: return false
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager? ?: return false
    clipboardManager.setPrimaryClip(ClipData.newPlainText("text", text))
    return true
}

fun Context.hideKeyboard(view: View) {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}