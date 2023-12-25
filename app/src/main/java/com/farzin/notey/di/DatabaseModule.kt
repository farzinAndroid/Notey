package com.farzin.notey.di

import android.content.Context
import androidx.room.Room
import com.farzin.notey.data.db.NoteDatabase
import com.farzin.notey.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        NoteDatabase::class.java,
        Constants.DB_NAME
    ).build()


    @Provides
    @Singleton
    fun provideNoteDao(database: NoteDatabase) = database.getDao()

}