package com.jivosite.jivosdk_android_sample.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jivosite.jivosdk_android_sample.R
import com.jivosite.sdk.ui.chat.JivoChatFragment

/**
 * Created on 4/19/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.jivoBtn)?.run {
            setOnClickListener {
                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, JivoChatFragment())
                    .commit()
            }
        }
    }
}