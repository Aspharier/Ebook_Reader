package com.example.ebook_reader.ui.library

import com.example.ebook_reader.data.local.entity.BookEntity

sealed interface LibraryUiState {
    data object Loading : LibraryUiState
    data object Empty : LibraryUiState
    data class Success(
            val books: List<BookEntity>,
            val categorizedBooks: Map<String, List<BookEntity>>
    ) : LibraryUiState
}
