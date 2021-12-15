package com.jivosite.sdk.model.repository.agent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jivosite.sdk.db.SdkDb
import com.jivosite.sdk.db.entities.DbAgent
import com.jivosite.sdk.model.cache.MemoryCache
import com.jivosite.sdk.model.pojo.agent.Agent
import com.jivosite.sdk.model.pojo.agent.AgentStatus
import com.jivosite.sdk.model.repository.StateRepository
import com.jivosite.sdk.support.async.Schedulers
import com.jivosite.sdk.support.vm.StateLiveData
import javax.inject.Inject

/**
 * Created on 21.09.2020.
 *
 * Реализация репозитория для хранения информации об агентах.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
class AgentRepositoryImpl @Inject constructor(
    private val schedulers: Schedulers,
    private val db: SdkDb
) : StateRepository<AgentState>(schedulers, "Agent", AgentState()), AgentRepository {

    private val cache = mutableMapOf<String, Agent>()
    private val cacheLive = MemoryCache<String, Agent>()

    override val state: AgentState
        get() = _state

    override val observableState: StateLiveData<AgentState>
        get() = _stateLive

    private val _hasAgentsOnline = MutableLiveData(false)
    override val hasAgentsOnline: LiveData<Boolean>
        get() = _hasAgentsOnline

    init {
        updateCacheFromDb()
    }

    override fun getAgent(agentId: String): Agent? = cache[agentId]

    override fun observeAgent(agentId: String): LiveData<Agent> = cacheLive[agentId]

    override fun setAgentStatus(agentId: String, status: String) = updateStateInRepositoryThread {
        val newStatus = AgentStatus.mapFrom(status)
        doBefore {
            cache[agentId]?.status != newStatus
        }
        transform { state ->
            val agent = cache[agentId]?.copy(status = newStatus, hasOnlineInChat = state.hasOnlineAgentsInChat)
                ?: Agent(id = agentId, status = newStatus, hasOnlineInChat = state.hasOnlineAgentsInChat)
            updateCache(agent)
            insertDb(agent)
            state.copy(agents = cache.entries.map { it.value })
        }
        doAfter { state ->
            updateHasAgentsOnline(state.agents)
        }
    }

    override fun setAgentName(agentId: String, name: String) = updateStateInRepositoryThread {
        doBefore { cache[agentId]?.name != name }
        transform { state ->
            val agent = cache[agentId]?.copy(name = name) ?: Agent(id = agentId, name = name)
            updateCache(agent)
            updateDb(agent)
            state.copy(agents = cache.entries.map { it.value })
        }
    }

    override fun setAgentTitle(agentId: String, title: String) = updateStateInRepositoryThread {
        doBefore { cache[agentId]?.title != title }
        transform { state ->
            val agent = cache[agentId]?.copy(title = title) ?: Agent(id = agentId, title = title)
            updateCache(agent)
            updateDb(agent)
            state.copy(agents = cache.entries.map { it.value })
        }
    }

    override fun setAgentPhoto(agentId: String, photo: String) = updateStateInRepositoryThread {
        doBefore { cache[agentId]?.photo != photo }
        transform { state ->
            val agent = cache[agentId]?.copy(photo = photo) ?: Agent(id = agentId, photo = photo)
            updateCache(agent)
            updateDb(agent)
            state.copy(agents = cache.entries.map { it.value })
        }
    }

    override fun setAgentStatusOffline() = updateStateInRepositoryThread {
        transform { state ->
            val agents = ArrayList<Agent>()
            agents.addAll(state.agents.map { it.copy(hasOnlineInChat = false) })
            agents.forEach { updateCache(it) }

            cache.entries.forEach {
                val agent = it.value.copy(status = AgentStatus.Offline)
                updateCache(agent)
            }
            state.copy(hasOnlineAgentsInChat = true, agents = agents)
        }
    }

    override fun onConnectionStateChanged() = updateStateInRepositoryThread {
        doBefore { it.hasOnlineAgentsInChat }
        transform { state ->
            state.copy(hasOnlineAgentsInChat = false)
        }
    }

    override fun clear() = updateStateInRepositoryThread {
        transform { AgentState() }
        doAfter { state ->
            cache.clear()
            cacheLive.clear()
            updateHasAgentsOnline(state.agents)
            clearDb()
        }
    }

    private fun updateCache(agent: Agent) {
        cache[agent.id] = agent
        cacheLive[agent.id] = agent
    }

    private fun updateCacheFromDb() {
        schedulers.io.execute {
            db.agentDao().getAgents().forEach {
                cache[it.id.toString()] = Agent(id = it.id.toString(), name = it.name, title = it.title, photo = it.photo)
                cacheLive[it.id.toString()] = Agent(id = it.id.toString(), name = it.name, title = it.title, photo = it.photo)
            }
        }
    }

    private fun insertDb(agent: Agent) {
        schedulers.io.execute {
            db.agentDao().insert(DbAgent(agent.id.toLong(), agent.name, agent.title, agent.photo))
        }
    }

    private fun updateDb(agent: Agent) {
        schedulers.io.execute {
            db.agentDao().update(DbAgent(agent.id.toLong(), agent.name, agent.title, agent.photo))
        }
    }

    private fun clearDb() {
        schedulers.io.execute {
            db.clearAllTables()
        }
    }

    private fun updateHasAgentsOnline(agents: List<Agent>) {
        _hasAgentsOnline.postValue(agents.any { it.status is AgentStatus.Online })
    }
}