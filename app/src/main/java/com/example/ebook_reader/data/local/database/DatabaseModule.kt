package com.example.ebook_reader.data.local.database

import android.content.Context
import androidx.room.Room
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
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ElibraryDatabase =
        Room.databaseBuilder(
            context,
            ElibraryDatabase::class.java,
            "elibrary_db"
        ).build()

    @Provides
    fun provideBookDao(
        database: ElibraryDatabase
    ) = database.bookDao()
}