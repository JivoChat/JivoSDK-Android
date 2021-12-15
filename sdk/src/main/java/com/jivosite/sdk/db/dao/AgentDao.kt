package com.jivosite.sdk.db.dao

import androidx.room.*
import com.jivosite.sdk.db.entities.DbAgent

/**
 * Created on 3/21/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
@Dao
interface AgentDao {

    @Query("""SELECT * FROM agent""")
    fun getAgents(): List<DbAgent>

    @Query("""SELECT * FROM agent WHERE id = :id""")
    fun findAgentById(id: Long): DbAgent

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(agent: DbAgent)

    @Update()
    fun update(agent: DbAgent)
}