package com.example.ebook_reader.data.repository

import com.example.ebook_reader.data.local.dao.BookDao
import com.example.ebook_reader.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookDao: BookDao
){
    fun getAllBooks(): Flow<List<BookEntity>> =
        bookDao.getAllBooks()

    suspend fun getBookById(id: String) = bookDao.getBookById(id)

    suspend fun insertBook(book: BookEntity) {
        bookDao.insertBook(book)
    }

    suspend fun updateBook(book: BookEntity) {
        bookDao.updateBook(book)
    }

    suspend fun deleteBook(book: BookEntity) {
        bookDao.deleteBook(book)
    }
}