package com.jivosite.sdk.model.repository.pagination

/**
 * Created on 1/31/21.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
data class PaginationState(
    val loading: Boolean = false,
    val hasNextPage: Boolean = false
)
