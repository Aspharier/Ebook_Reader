package com.example.ebook_reader.ui.library

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.currentComposer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ebook_reader.data.local.entity.BookEntity
import com.example.ebook_reader.data.repository.BookRepository
import com.example.ebook_reader.utils.queryFileName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {

    val uiState: StateFlow<LibraryUiState> =
        repository.getAllBooks()
            .map { books ->
                if (books.isEmpty()) {
                    LibraryUiState.Empty
                } else {
                    LibraryUiState.Success(books)
                }
            }
            .onStart {
                emit(LibraryUiState.Loading)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = LibraryUiState.Loading
            )

    fun importPdfs(
        context: Context,
        uris: List<Uri>
    ) {
        viewModelScope.launch {
            uris.forEach { uri ->
                val fileName = queryFileName(context, uri) ?: "Untitled.pdf"
                val bookId = UUID.randomUUID().toString()

                val pdfFile = File(
                    context.filesDir,
                    "$bookId.pdf"
                )

                context.contentResolver.openInputStream(uri)?.use { input ->
                    pdfFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                val fileSize = pdfFile.length()

                val book = BookEntity(
                    id = bookId,
                    title = fileName.removeSuffix(".pdf"),
                    author = null,
                    filePath = pdfFile.absolutePath,
                    coverImagePath = null,
                    totalPages = 0,
                    currentPage = 0,
                    progress = 0f,
                    dateAdded = System.currentTimeMillis(),
                    lastReadDate = 0L,
                    fileSize = fileSize
                )

                repository.insertBook(book)
            }
        }
    }
}

