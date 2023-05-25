package com.jivosite.sdk.model.pojo.rate

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created on 01.02.2023.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
@JsonClass(generateAdapter = true)
data class RateSettings(
    @Json(name = "type")
    val type: Type,
    @Json(name = "icon")
    val icon: Icon,
    @Json(name = "condition_name")
    val conditionName: String,
    @Json(name = "condition_value")
    val conditionValue: Int,
    @Json(name = "custom_title")
    val customTitle: String? = null,
    @Json(name = "good_rate_title")
    val goodRateTitle: String? = null,
    @Json(name = "bad_rate_title")
    val badRateTitle: String? = null
) {

    enum class Type(val type: String) {
        UNKNOWN("unknown"),
        TWO("two"),
        THREE("three"),
        FIVE("five");

        companion object {
            fun fromValue(value: String): Type {
                return values().firstOrNull { it.type == value } ?: UNKNOWN
            }
        }
    }

    enum class Icon(val icon: String) {
        UNKNOWN("unknown"),
        SMILE("smile"),
        STAR("star");

        companion object {
            fun fromValue(value: String): Icon {
                return values().firstOrNull { it.icon == value } ?: UNKNOWN
            }
        }
    }

    enum class Rate(val rate: String) {
        GOOD("good"),
        GOOD_NORMAL("goodnormal"),
        NORMAL("normal"),
        BAD_NORMAL("badnormal"),
        BAD("bad")
    }
}

