package com.jivosite.sdk.ui.chat

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.jivosite.sdk.R

/**
 * Created on 1/20/21.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class JivoChatActivity : AppCompatActivity(R.layout.activity_jivo_chat) {

    override fun onCreate(savedInstanceState: Bundle?) {
        //enableEdgeToEdge()
        super.onCreate(savedInstanceState)
       // WindowCompat.setDecorFitsSystemWindows(window, false)
    }
}