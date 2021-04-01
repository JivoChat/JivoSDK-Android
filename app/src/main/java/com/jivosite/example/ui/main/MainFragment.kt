package com.jivosite.example.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jivosite.example.R

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)?.also {
            it.findViewById<View>(R.id.jivoBtn)?.run {
                setOnClickListener {
                    findNavController().navigate(R.id.action_pageMain_to_jivoChat)
                }
            }
        }
    }
}