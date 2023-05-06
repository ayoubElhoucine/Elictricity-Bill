package com.ayoub.electricitybill.di

import com.ayoub.electricitybill.data.network.Api
import com.ayoub.electricitybill.data.network.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideApi(
        remoteDataSource: RemoteDataSource,
    ): Api {
        return remoteDataSource.buildApi(Api::class.java)
    }

}