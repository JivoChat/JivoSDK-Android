package com.jivosite.sdk.support.resources

import android.content.res.Resources
import androidx.annotation.StringRes

/**
 * Created on 02.07.2025.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
sealed class TextResource {
    companion object {

        @JvmStatic
        fun empty(): TextResource = EmptyTextResource

        @JvmStatic
        fun fromString(text: String): TextResource = StringTextResource(text)

        @JvmStatic
        fun fromId(@StringRes id: Int): TextResource = IdTextResource(id)
    }

    val isEmpty: Boolean
        get() = this is EmptyTextResource

    fun asString(resources: Resources, placeholder: String = ""): String {
        return when (this) {
            is EmptyTextResource -> placeholder
            is StringTextResource -> text
            is IdTextResource -> resources.getString(id)
        }
    }
}

private data object EmptyTextResource : TextResource()
private data class StringTextResource(val text: String) : TextResource()
private data class IdTextResource(@StringRes val id: Int) : TextResource()
