package com.jivosite.sdk.model.repository.rating

import com.jivosite.sdk.model.pojo.rate.RateSettings
import com.jivosite.sdk.model.pojo.socket.SocketMessage
import com.jivosite.sdk.model.repository.StateRepository
import com.jivosite.sdk.model.storage.SharedStorage
import com.jivosite.sdk.socket.transmitter.Transmitter
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.ext.toJson
import com.jivosite.sdk.support.vm.StateLiveData
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import javax.inject.Inject

/**
 * Created on 20.01.2023.
 *
 * @author Aleksandr Tavtorkin (tavtorkin@jivosite.com)
 */
class RatingRepositoryImpl @Inject constructor(
    schedulers: Schedulers,
    private val storage: SharedStorage,
    private val transmitter: Transmitter,
    private val moshi: Moshi
) : StateRepository<RatingState>(schedulers, "Rating", RatingState()), RatingRepository {

    override val observableState: StateLiveData<RatingState>
        get() = _stateLive

    override fun setRateSettings(rateSettings: RateSettings?) = updateStateInRepositoryThread {
        transform { state ->
            state.copy(rateSettings = rateSettings)
        }
    }

    override fun setChatId(chatId: String) = updateStateInRepositoryThread {
        doBefore { chatId.isNotBlank() }
        transform { state ->
            state.copy(ratingFormState = RatingFormState.Ready, timestamp = System.currentTimeMillis() / 1000)
        }
        doAfter { storage.chatId = chatId }
    }

    override fun setRate(rate: String) = updateStateInRepositoryThread {
        transform { state ->
            when (state.ratingFormState) {
                is RatingFormState.Ready -> {
                    state.copy(ratingFormState = RatingFormState.Draft(rate = rate))
                }

                is RatingFormState.Draft -> {
                    state.copy(ratingFormState = state.ratingFormState.copy(rate = rate))
                }

                else -> {
                    state
                }
            }
        }
    }

    override fun setComment(comment: String) = updateStateInRepositoryThread {
        doBefore { state ->
            state.ratingFormState is RatingFormState.Draft
        }
        transform { state ->
            state.copy(ratingFormState = (state.ratingFormState as RatingFormState.Draft).copy(comment = comment))
        }
    }

    override fun sendRating() = updateStateInRepositoryThread {
        transform { state ->
            transmitter.sendMessage(
                SocketMessage.rating(
                    data = (state.ratingFormState as RatingFormState.Draft).let {
                        moshi.toJson(Rating(it.rate, it.comment ?: ""))
                    },
                    chatId = storage.chatId
                )
            )
            state.copy(ratingFormState = RatingFormState.Sent(state.ratingFormState.rate))
        }
        doAfter {
            storage.chatId = ""
        }
    }

    override fun close() = updateStateInRepositoryThread {
        transform { state ->
            storage.chatId = ""
            state.copy(ratingFormState = RatingFormState.Initial)
        }
    }

    override fun clear() = updateStateInRepositoryThread {
        transform {
            storage.chatId = ""
            it.copy(ratingFormState = RatingFormState.Initial)
        }
    }
}

@JsonClass(generateAdapter = true)
data class Rating(
    val rate: String = "",
    val comment: String = ""
)