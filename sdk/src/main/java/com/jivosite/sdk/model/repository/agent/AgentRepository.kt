package com.jivosite.sdk.model.repository.agent

import androidx.lifecycle.LiveData
import com.jivosite.sdk.model.pojo.agent.Agent
import com.jivosite.sdk.support.vm.StateLiveData

/**
 * Created on 21.09.2020.
 *
 * Репозиторий для хранения информации об агентах.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
interface AgentRepository {

    val state: AgentState

    val observableState: StateLiveData<AgentState>

    /**
     * Наличие агентов онлайн.
     */
    val hasAgentsOnline: LiveData<Boolean>

    /**
     * Получение информации об агенте, той что есть на текущий момент.
     * @param agentId Идентификатор агента.
     * @return Информация об агенте, если ее нет, то возвращается null.
     */
    fun getAgent(agentId: String): Agent?

    /**
     * Получение актуальной информации об агенте.
     * @param agentId Идентификатор агента.
     * @return Актуальная информация об агенте и ее обновление.
     */
    fun observeAgent(agentId: String): LiveData<Agent>

    /**
     * Установка информации о статусе агента.
     * @param agentId Идентификатор агента.
     * @param status Новый статус агента.
     */
    fun setAgentStatus(agentId: String, status: String)

    /**
     * Установка имени агента.
     * @param agentId Идентификатор агента.
     * @param name Имя агента.
     */
    fun setAgentName(agentId: String, name: String)

    /**
     * Установка должности агента.
     * @param agentId Идентификатор агента.
     * @param title Должность.
     */
    fun setAgentTitle(agentId: String, title: String)

    /**
     *  Установка фотографии агента.
     * @param agentId Идентификатор агента.
     * @param photo URL-адрес изображения.
     */
    fun setAgentPhoto(agentId: String, photo: String)

    /**
     *  Установка статуса агентов в offline
     */
    fun setAgentStatusOffline()

    /**
     *   Событие об изменении статуса покдлючения к сокету
     */
    fun onConnectionStateChanged()

    fun clear()
}