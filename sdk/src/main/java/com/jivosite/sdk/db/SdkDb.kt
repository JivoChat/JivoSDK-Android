package com.jivosite.sdk.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jivosite.sdk.db.SdkDb.Companion.DATABASE_VERSION
import com.jivosite.sdk.db.dao.AgentDao
import com.jivosite.sdk.db.entities.DbAgent

/**
 * Created on 3/21/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
@Database(entities = [DbAgent::class], version = DATABASE_VERSION, exportSchema = false)
abstract class SdkDb : RoomDatabase() {
    companion object {
        const val DATABASE_NAME: String = "jivo-sdk-db"
        const val DATABASE_VERSION = 1
    }

    abstract fun agentDao(): AgentDao
}