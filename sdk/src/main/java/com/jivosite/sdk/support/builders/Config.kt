package com.jivosite.sdk.support.builders

import android.app.PendingIntent
import android.net.Uri
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.jivosite.sdk.support.resources.TextResource
import com.jivosite.sdk.ui.chat.JivoChatFragment

/**
 * Created on 07.06.2021.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class Config private constructor(
    val title: TextResource,
    val subtitle: TextResource,
    val welcomeMessage: TextResource,
    val openNotificationCallback: (() -> PendingIntent)?,
    val onBackPressedCallback: (JivoChatFragment.() -> Unit)?,
    val uriNotificationSound: Uri?,
    val notificationSmallIcon: Int?,
    val notificationColorIcon: Int?,
    val offlineMessage: TextResource,
    val useRattingStringsRes: Boolean = false,
    val notificationTitle: TextResource
) {

    class Builder {

        private var title: TextResource = TextResource.empty()

        private var subtitle: TextResource = TextResource.empty()

        private var welcomeMessage: TextResource = TextResource.empty()

        private var openNotificationCallback: (() -> PendingIntent)? = null

        private var onBackPressedCallback: (JivoChatFragment.() -> Unit)? = null

        private var uriNotificationSound: Uri? = null

        private var offlineMessage: TextResource = TextResource.empty()

        @DrawableRes
        private var notificationSmallIcon: Int? = null

        @ColorRes
        private var notificationColorIcon: Int? = null

        private var useRattingStringsRes: Boolean = false

        private var notificationTitle: TextResource = TextResource.empty()

        fun setTitle(@StringRes titleResId: Int) =
            apply { this.title = TextResource.fromId(titleResId) }

        fun setTitleString(title: String) =
            apply { this.title = TextResource.fromString(title) }

        fun setSubtitle(@StringRes subtitleResId: Int) =
            apply { this.subtitle = TextResource.fromId(subtitleResId) }

        fun setSubtitleString(subtitle: String) =
            apply { this.subtitle = TextResource.fromString(subtitle) }

        fun setWelcomeMessage(@StringRes welcomeMessageResId: Int) =
            apply { this.welcomeMessage = TextResource.fromId(welcomeMessageResId) }

        fun setWelcomeMessageString(welcomeMessage: String) =
            apply { this.welcomeMessage = TextResource.fromString(welcomeMessage) }

        fun setOpenNotification(callback: (() -> PendingIntent)) = apply { this.openNotificationCallback = callback }

        fun setOnBackPressed(callback: JivoChatFragment.() -> Unit) = apply { this.onBackPressedCallback = callback }

        fun setUriNotificationSound(uri: Uri) = apply { this.uriNotificationSound = uri }

        fun setNotificationSmallIcon(@DrawableRes notificationSmallIcon: Int) =
            apply { this.notificationSmallIcon = notificationSmallIcon }

        fun setNotificationColorIcon(@ColorRes notificationColorIcon: Int) = apply {
            this.notificationColorIcon = notificationColorIcon
        }

        fun setOfflineMessage(@StringRes offlineMessageResId: Int) =
            apply { this.offlineMessage = TextResource.fromId(offlineMessageResId) }

        fun setOfflineMessageString(offlineMessage: String) =
            apply { this.offlineMessage = TextResource.fromString(offlineMessage) }

        fun useRattingStringsRes() = apply { this.useRattingStringsRes = true }

        fun setNotificationTitle(@StringRes notificationTitleResId: Int) =
            apply { this.notificationTitle = TextResource.fromId(notificationTitleResId) }

        fun setNotificationTitleString(notificationTitle: String) =
            apply { this.notificationTitle = TextResource.fromString(notificationTitle) }

        fun build() = Config(
            title,
            subtitle,
            welcomeMessage,
            openNotificationCallback,
            onBackPressedCallback,
            uriNotificationSound,
            notificationSmallIcon,
            notificationColorIcon,
            offlineMessage,
            useRattingStringsRes,
            notificationTitle
        )

    }

}