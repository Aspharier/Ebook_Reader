package com.example.ebook_reader.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey val id: String,
    val title: String,
    val author: String?,
    val filePath: String,
    val coverImagePath: String?,
    val totalPages: Int,
    val currentPage: Int,
    val progress: Float,
    val dateAdded: Long,
    val lastReadDate: Long,
    val fileSize: Long
)
