package com.example.ebook_reader.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private val MIGRATION_1_2 =
            object : Migration(1, 2) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL(
                            "ALTER TABLE books ADD COLUMN category TEXT NOT NULL DEFAULT 'Other'"
                    )
                }
            }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ElibraryDatabase =
            Room.databaseBuilder(context, ElibraryDatabase::class.java, "elibrary_db")
                    .addMigrations(MIGRATION_1_2)
                    .build()

    @Provides fun provideBookDao(database: ElibraryDatabase) = database.bookDao()
}
