package com.example.ebook_reader.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.ebook_reader.data.local.dao.BookDao
import com.example.ebook_reader.data.local.entity.BookEntity

@Database(entities = [BookEntity::class], version = 2, exportSchema = false)
abstract class ElibraryDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}
