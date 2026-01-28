package com.example.ebook_reader.ui.reader

import android.graphics.Bitmap

data class PdfPage(
    val index: Int,
    val bitmap: Bitmap
)

sealed interface ReaderUiState {

    data object Loading : ReaderUiState
    data class Ready(
        val totalPages: Int,
        val currentPage: Int
    ) : ReaderUiState
}