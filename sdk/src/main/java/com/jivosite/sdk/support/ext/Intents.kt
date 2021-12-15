package com.jivosite.sdk.support.ext

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContextCompat
import com.jivosite.sdk.ui.imageviewer.ImageViewerActivity
import org.jetbrains.annotations.NotNull

/**
 * Cre
 * ted on 12.11.2020.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class Intents {

    companion object {
        fun downloadFile(context: Context, path: String?, @NotNull fileName: String) {
            if (path.isNullOrBlank() || !path.startsWith("https")) return

            if (ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
                val request = DownloadManager.Request(Uri.parse(path))
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)

                (context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager)
                    .enqueue(request)
            } else {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(path))

                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                }
            }
        }

        fun showActivityImageViewer(context: Context, path: String, name: String) {
            ImageViewerActivity.show(context, path, name)
        }
    }
}
