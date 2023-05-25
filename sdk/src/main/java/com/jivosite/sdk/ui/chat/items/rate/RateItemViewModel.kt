package com.jivosite.sdk.ui.chat.items.rate

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jivosite.sdk.model.repository.rating.RatingRepository
import com.jivosite.sdk.model.repository.rating.RatingState
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.ui.chat.items.RatingEntry
import com.jivosite.sdk.ui.chat.items.message.general.ChatEntryViewModel
import javax.inject.Inject

/**
 * Created on 20.01.2023.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class RateItemViewModel @Inject constructor(
    val storage: SharedStorage,
    private val ratingRepository: RatingRepository
) : ChatEntryViewModel<RatingEntry>() {

    private val state: LiveData<RatingState> = Transformations.map(_entry) { entry ->
        entry.state
    }

    val ratingState: LiveData<RatingState> = Transformations.map(state) {
        it
    }

    val title: LiveData<String> = Transformations.map(state) {
        it.rateSettings?.customTitle
    }

    val type: LiveData<String> = Transformations.map(state) {
        it.rateSettings?.type?.type
    }

    val icon: LiveData<String> = Transformations.map(state) {
        it.rateSettings?.icon?.icon
    }

    fun setRate(rating: String) {
        ratingRepository.setRate(rating)
    }

    fun setComment(comment: String) {
        ratingRepository.setComment(comment)
    }

    fun sendRate() {
        ratingRepository.sendRating()
    }

    fun close() {
        ratingRepository.close()
    }
}