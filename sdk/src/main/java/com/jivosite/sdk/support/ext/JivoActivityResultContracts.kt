package com.jivosite.sdk.support.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

/**
 * Created on 05.04.2024.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class TakePicture : ActivityResultContract<Uri, Uri?>() {

    private lateinit var outputUri: Uri

    override fun createIntent(context: Context, input: Uri): Intent {
        outputUri = input
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (resultCode == Activity.RESULT_OK && this::outputUri.isInitialized) outputUri else null
    }
}
