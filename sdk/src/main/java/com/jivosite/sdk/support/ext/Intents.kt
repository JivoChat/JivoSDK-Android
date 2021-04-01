package com.jivosite.sdk.support.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.jivosite.sdk.ui.imageviewer.ImageViewerActivity

/**
 * Cre
 * ted on 12.11.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
class Intents {

    companion object {
        fun downloadFile(context: Context, url: String?) {
            if (url.isNullOrBlank()) return
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }

        fun showActivityImageViewer(context: Context, url: String, mediaName: String) {
            ImageViewerActivity.show(context, url, mediaName)
        }
    }
}