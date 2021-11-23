package com.jivosite.sdk.support.ext

import android.webkit.MimeTypeMap
import com.jivosite.sdk.model.pojo.file.SupportFileTypes
import java.net.URLDecoder
import java.security.MessageDigest
import java.util.regex.Pattern

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

fun String.getSupportFileType(): String? {
    val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(this) ?: ""
    return SupportFileTypes.SUPPORT_FILE_TYPES[extension] ?: SupportFileTypes.TYPE_UNKNOWN
}

fun String?.parseContentDisposition(): String {
    if (this == null) return ""

    var s = ""

    try {
        val pattern = Pattern.compile(
            "attachment; filename(?:\\*=[a-zA-Z0-9_-]+'[\\w_-]*?'|=)([\"']?)(.+)(\\1)",
            Pattern.CASE_INSENSITIVE
        )
        pattern.matcher(this).run {
            if (matches()) {
                s = group(2) ?: ""
            }
        }
    } catch (e: IllegalStateException) {
    }

    return if (s.isBlank()) s else URLDecoder.decode(s, "UTF-8")
}

fun String.toMD5() = MessageDigest.getInstance("MD5")
    .digest(this.toByteArray())
    .joinToString("") { "%02x".format(it) }



