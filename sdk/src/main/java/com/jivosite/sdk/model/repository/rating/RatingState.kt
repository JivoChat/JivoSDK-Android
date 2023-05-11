package com.jivosite.sdk.model.repository.rating

import com.jivosite.sdk.model.pojo.rate.RateSettings

/**
 * Created on 20.01.2023.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
data class RatingState(
    val rateSettings: RateSettings? = null,
    val ratingFormState: RatingFormState = RatingFormState.Initial,
    val timestamp: Long = 0
) {

    val size: Int
        get() = if (ratingFormState is RatingFormState.Initial) 0 else 1
}

sealed class RatingFormState {

    object Initial : RatingFormState()

    object Ready : RatingFormState()

    data class Draft(
        val rate: String,
        val comment: String? = null,
    ) : RatingFormState()

    data class Sent(
        val rate: String
    ) : RatingFormState()
}