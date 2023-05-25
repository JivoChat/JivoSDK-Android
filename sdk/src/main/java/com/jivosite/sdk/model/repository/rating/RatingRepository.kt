package com.jivosite.sdk.model.repository.rating

import com.jivosite.sdk.model.pojo.rate.RateSettings
import com.jivosite.sdk.support.vm.StateLiveData

/**
 * Created on 20.01.2023.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
interface RatingRepository {

    val observableState: StateLiveData<RatingState>

    fun setChatId(chatId: String)

    fun sendRating()

    fun setRateSettings(rateSettings: RateSettings?)

    fun setRate(rate: String)

    fun setComment(comment: String)

    fun close()

    fun clear()
}