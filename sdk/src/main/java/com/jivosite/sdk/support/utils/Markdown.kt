package com.jivosite.sdk.support.utils

import android.webkit.MimeTypeMap
import com.jivosite.sdk.model.pojo.message.HistoryMessage
import com.jivosite.sdk.socket.handler.delegates.TextPlainDelegate
import java.util.regex.Pattern

/**
 * Created on 03.11.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */

val imagePattern: Pattern by lazy { Pattern.compile("^!\\[(.*)]\\((.*)\\)") }

fun transformMessageIfNeed(message: HistoryMessage): HistoryMessage {
    val data = message.data

    if (message.type == TextPlainDelegate.TYPE ) {
        val (title, link)  = imagePattern.toRegex().find(data)?.destructured ?: return message
        if (link.isBlank()) return message

        val mimeType = MimeTypeMap.getFileExtensionFromUrl(link).let {
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(it)
        }
        if (mimeType.isNullOrBlank()) return message
        return message.copy(type = mimeType, data = link)
    } else {
        return message
    }
}