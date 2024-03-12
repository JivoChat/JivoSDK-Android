package com.jivosite.sdk.support.utils

fun getEngineInfo(): String {

    return when {
        getClassName("io.flutter.app.FlutterApplication").isNotBlank() -> "flutter"
        getClassName("com.facebook.react.ReactApplication").isNotBlank() -> "reactnative"
        getClassName("org.jetbrains.kotlin.commonizer.CliCommonizer").isNotBlank()-> "kmp"
        else -> "native"
    }
}

private fun getClassName(import: String): String {
    val name = try {
        Class.forName(import).name
    } catch (e: ClassNotFoundException) {
        ""
    }
    return name
}