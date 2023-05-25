package com.jivosite.sdk.support.ext

import android.util.Patterns
import android.webkit.MimeTypeMap
import com.jivosite.sdk.Jivo
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
        s = URLDecoder.decode(s, "UTF-8")
    } catch (e: Exception) {
        Jivo.e(e, "Content disposition parsing problem")
    }
    return s
}

fun String.toMD5() = MessageDigest.getInstance("MD5")
    .digest(this.toByteArray())
    .joinToString("") { "%02x".format(it) }

fun String.isEmailValid(): Boolean {
    return this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isPhoneValid(): Boolean {
    return this.isNotBlank() && Patterns.PHONE.matcher(this).matches()
}

fun String.verifyHostName(): Boolean {
    return this.isNotBlank() && Pattern.compile("^([a-z0-9]+(-[a-z0-9]+)*\\.)+[a-z]{2,}\$").matcher(this.lowercase()).matches()
}




