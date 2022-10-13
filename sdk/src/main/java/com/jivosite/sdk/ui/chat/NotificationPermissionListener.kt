package com.jivosite.sdk.ui.chat

/**
 * Created on 05.10.2022.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
interface NotificationPermissionListener {

    fun onNotificationPermissionGranted(isGranted: Boolean)
}