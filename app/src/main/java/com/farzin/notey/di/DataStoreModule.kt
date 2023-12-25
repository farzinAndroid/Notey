package com.farzin.notey.di

import android.content.Context
import com.farzin.notey.data.data_store.DataStoreRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {


    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStoreRepoImpl =
        DataStoreRepoImpl(context)

}