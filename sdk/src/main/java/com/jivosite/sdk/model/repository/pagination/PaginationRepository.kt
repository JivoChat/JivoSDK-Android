package com.jivosite.sdk.model.repository.pagination

/**
 * Created on 1/31/21.
 *
 * Состояние пагинации для истории сообщений в чате.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
interface PaginationRepository {

    /**
     * Актуальное состояние.
     */
    val state: PaginationState

    /**
     * Установка состояния загрузки истории.
     */
    fun loadingStarted()

    /**
     * Установка наличия следующей страницы.
     * @param hasNextPage true если есть следующая страница.
     */
    fun setHasNextPage(hasNextPage: Boolean)

    /**
     * Обработка пришедшего из истории сообщения.
     */
    fun handleHistoryMessage()

    fun clear()
}