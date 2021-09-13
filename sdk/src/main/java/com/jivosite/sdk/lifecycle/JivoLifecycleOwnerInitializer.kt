package com.jivosite.sdk.lifecycle

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.lifecycle.JivoLifecycleOwner

/**
 * Created on 13.09.2021.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
class JivoLifecycleOwnerInitializer : ContentProvider() {

    override fun onCreate(): Boolean {
        context?.run {
            JivoLifecycleOwner.init(this)
        }
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? = null

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?) = 0

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ) = 0
}