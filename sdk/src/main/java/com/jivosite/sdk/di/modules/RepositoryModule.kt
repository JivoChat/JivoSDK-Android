package com.jivosite.sdk.di.modules

import com.jivosite.sdk.logger.LogsRepository
import com.jivosite.sdk.logger.LogsRepositoryImpl
import com.jivosite.sdk.model.repository.agent.AgentRepository
import com.jivosite.sdk.model.repository.agent.AgentRepositoryImpl
import com.jivosite.sdk.model.repository.chat.ChatStateRepository
import com.jivosite.sdk.model.repository.chat.ChatStateRepositoryImpl
import com.jivosite.sdk.model.repository.connection.ConnectionStateRepository
import com.jivosite.sdk.model.repository.connection.ConnectionStateRepositoryImpl
import com.jivosite.sdk.model.repository.history.HistoryRepository
import com.jivosite.sdk.model.repository.history.HistoryRepositoryImpl
import com.jivosite.sdk.model.repository.pagination.PaginationRepository
import com.jivosite.sdk.model.repository.pagination.PaginationRepositoryImpl
import com.jivosite.sdk.model.repository.profile.ProfileRepository
import com.jivosite.sdk.model.repository.profile.ProfileRepositoryImpl
import com.jivosite.sdk.model.repository.send.SendMessageRepository
import com.jivosite.sdk.model.repository.send.SendMessageRepositoryImpl
import com.jivosite.sdk.model.repository.typing.TypingRepository
import com.jivosite.sdk.model.repository.typing.TypingRepositoryImpl
import com.jivosite.sdk.model.repository.upload.UploadRepository
import com.jivosite.sdk.model.repository.upload.UploadRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Created on 15.09.2020.
 *
 * @author Alexander Tavtorkin (av.tavtorkin@gmail.com)
 */
@Module
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun provideLogsRepository(repository: LogsRepositoryImpl): LogsRepository

    @Binds
    @Singleton
    abstract fun provideConnectionStateRepository(repository: ConnectionStateRepositoryImpl): ConnectionStateRepository

    @Binds
    @Singleton
    abstract fun provideProfileRepository(repository: ProfileRepositoryImpl): ProfileRepository

    @Binds
    @Singleton
    abstract fun provideAgentRepository(repository: AgentRepositoryImpl): AgentRepository

    @Binds
    @Singleton
    abstract fun provideHistoryRepository(repository: HistoryRepositoryImpl): HistoryRepository

    @Binds
    @Singleton
    abstract fun provideChatStateRepository(repository: ChatStateRepositoryImpl): ChatStateRepository

    @Binds
    @Singleton
    abstract fun providePaginationRepository(repository: PaginationRepositoryImpl): PaginationRepository

    @Binds
    @Singleton
    abstract fun provideSendMessageRepository(repository: SendMessageRepositoryImpl): SendMessageRepository

    @Binds
    @Singleton
    abstract fun provideTypingRepository(repository: TypingRepositoryImpl): TypingRepository

    @Binds
    @Singleton
    abstract fun provideUploadRepository(repository: UploadRepositoryImpl): UploadRepository
}