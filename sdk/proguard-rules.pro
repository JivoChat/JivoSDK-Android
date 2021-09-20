# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Android data binding
-dontwarn androidx.databinding.**
-keep class androidx.databinding.* { *; }
-keep class * extends androidx.databinding.DataBinderMapper { *; }

-keep public class com.jivosite.sdk.Jivo { *; }
-keep public class com.jivosite.sdk.model.repository.history.NewMessageListener { *; }
-keep public class com.jivosite.sdk.ui.chat.JivoChatFragment { *; }
-keep public class com.jivosite.sdk.ui.views.JivoChatButton { *; }

-keep public class com.jivosite.sdk.push.JivoFirebaseMessagingService
-keep public class com.jivosite.sdk.push.RemoteMessageHandler

-keep class com.jivosite.sdk.support.builders.ClientInfo { *; }
-keep class com.jivosite.sdk.support.builders.ClientInfo$Builder { *; }
-keep class com.jivosite.sdk.support.builders.Config { *; }
-keep class com.jivosite.sdk.support.builders.Config$Builder { *; }
-keep class com.jivosite.sdk.support.builders.Config$Color { *; }

-keep public class com.jivosite.sdk.fileprovider.JivoSdkFileProvider { *; }
