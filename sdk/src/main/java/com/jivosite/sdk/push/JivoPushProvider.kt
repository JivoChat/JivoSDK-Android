package com.jivosite.sdk.push

/**
 * Created on 14.05.2026.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
enum class JivoPushProvider(val value: String) {
    FCM("android"),
    HMS("huawei"),
    RUSTORE("rustore")
}