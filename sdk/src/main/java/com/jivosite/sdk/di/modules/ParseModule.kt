package com.jivosite.sdk.di.modules

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created on 06.09.2020.
 *
 * @author Alexandr Shibelev (shibelev@jivosite.com)
 */
@Module
class ParseModule {

    @Provides
    @Singleton
    fun provideParser(): Moshi {
        return Moshi.Builder()
            .build()
    }
}