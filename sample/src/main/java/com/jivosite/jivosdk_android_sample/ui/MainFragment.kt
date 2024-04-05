package com.jivosite.jivosdk_android_sample.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.jivosite.jivosdk_android_sample.R
import com.jivosite.sdk.ui.chat.JivoChatFragment

/**
 * Created on 4/19/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class MainFragment : Fragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.jivoBtn)?.run {
            setOnClickListener {
                parentFragmentManager
                    .beginTransaction()
                    .add(R.id.container, JivoChatFragment())
                    .commit()
            }
        }
    }
}