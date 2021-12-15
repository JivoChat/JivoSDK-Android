package com.jivosite.sdk.di.modules

import androidx.room.Room
import com.jivosite.sdk.db.SdkDb
import com.jivosite.sdk.db.dao.AgentDao
import com.jivosite.sdk.model.SdkContext
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created on 3/21/21.
 *
 * @author Alexander Tavtorkin (tavtorkin@jivosite.com)
 */
@Module
class DbModule {

    @Singleton
    @Provides
    fun provideSdkDb(sdkContext: SdkContext): SdkDb = Room.databaseBuilder(
        sdkContext.appContext,
        SdkDb::class.java,
        SdkDb.DATABASE_NAME
    ).build()

    @Provides
    fun provideAgentsDao(db: SdkDb): AgentDao = db.agentDao()
}