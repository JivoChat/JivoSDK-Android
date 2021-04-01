package com.jivosite.sdk.di.service.modules

import com.jivosite.sdk.di.service.ServiceScope
import com.jivosite.sdk.di.service.StateKey
import com.jivosite.sdk.socket.states.ServiceState
import com.jivosite.sdk.socket.states.ServiceStateFactory
import com.jivosite.sdk.socket.states.items.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Provider

/**
 * Created on 10.09.2020.
 *
 * @author Alexandr Shibelev (av.shibelev@gmail.com)
 */
@Module(includes = [StateModule.Bindings::class])
class StateModule {

    @Module
    interface Bindings {

        @Binds
        @IntoMap
        @StateKey(InitialState::class)
        fun provideInitialState(state: InitialState): ServiceState

        @Binds
        @IntoMap
        @StateKey(ConnectingState::class)
        fun provideConnectingState(state: ConnectingState): ServiceState

        @Binds
        @IntoMap
        @StateKey(ConnectedState::class)
        fun provideConnectedState(state: ConnectedState): ServiceState

        @Binds
        @IntoMap
        @StateKey(DisconnectedState::class)
        fun provideDisconnectedState(state: DisconnectedState): ServiceState

        @Binds
        @IntoMap
        @StateKey(StoppedState::class)
        fun provideStoppedState(state: StoppedState): ServiceState
    }

    @ServiceScope
    @Provides
    fun provideServiceStateFactory(statesMap: Map<Class<out ServiceState>, @JvmSuppressWildcards Provider<ServiceState>>): ServiceStateFactory {
        return ServiceStateFactory(statesMap)
    }
}