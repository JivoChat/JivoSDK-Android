package com.jivosite.sdk.model.repository.pagination

import android.os.Handler
import android.os.Looper
import com.jivosite.sdk.model.repository.StateRepository
import com.jivosite.sdk.support.async.Schedulers
import javax.inject.Inject

/**
 * Created on 1/31/21.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
class PaginationRepositoryImpl @Inject constructor(
    schedulers: Schedulers
) : StateRepository<PaginationState>(schedulers, "Pagination", PaginationState()), PaginationRepository {

    companion object {
        private const val WAIT_TIMEOUT = 500L
        private const val PAGE_COUNT = 30
    }

    private val handler = Handler(Looper.getMainLooper())
    private val stopLoadingCallback = Runnable {
        val hasNextPage = loadedCount == PAGE_COUNT
        updateStateInDispatchingThread {
            transform { state -> state.copy(loading = false, hasNextPage = hasNextPage) }
            doAfter { loadedCount = 0 }
        }
    }
    private var loadedCount = 0

    override val state: PaginationState
        get() = _state

    override fun loadingStarted() = updateStateInDispatchingThread {
        doBefore { state -> !state.loading }
        transform { state -> state.copy(loading = true) }
        doAfter {
            loadedCount = 0
            handler.removeCallbacks(stopLoadingCallback)
            handler.postDelayed(stopLoadingCallback, WAIT_TIMEOUT)
        }
    }

    override fun setHasNextPage(hasNextPage: Boolean) = updateStateInDispatchingThread {
        doBefore { state -> state.hasNextPage != hasNextPage }
        transform { state ->
            if (hasNextPage) {
                state.copy(hasNextPage = hasNextPage)
            } else {
                state.copy(hasNextPage = hasNextPage, loading = false)
            }
        }
    }

    override fun handleHistoryMessage() = handleInDispatchingThread {
        handler.removeCallbacks(stopLoadingCallback)

        loadedCount++
        if (loadedCount == PAGE_COUNT) {
            stopLoadingCallback.run()
        } else {
            handler.postDelayed(stopLoadingCallback, WAIT_TIMEOUT)
        }
    }

    override fun clear() = updateStateInDispatchingThread {
        transform { PaginationState() }
        doAfter {
            loadedCount = 0
            handler.removeCallbacks(stopLoadingCallback)
        }
    }
}