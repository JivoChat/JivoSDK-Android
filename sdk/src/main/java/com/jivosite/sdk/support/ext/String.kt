package com.jivosite.sdk.support.ext

import android.webkit.MimeTypeMap
import com.jivosite.sdk.model.pojo.file.SupportFileTypes

fun String.cutName(): String {
    val spacePosition = indexOf(" ")
    return if (spacePosition > 0 && spacePosition < length - 1) {
        substring(0, spacePosition + 2) + "."
    } else {
        this
    }
}

fun String.getFileType(): String {
    val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(this) ?: ""
    return SupportFileTypes.FILE_TYPES[extension] ?: SupportFileTypes.TYPE_UNKNOWN
}

fun String.getSupportFileType(): String {
    val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(this) ?: ""
    return SupportFileTypes.SUPPORT_FILE_TYPES[extension] ?: SupportFileTypes.TYPE_UNKNOWN
}



