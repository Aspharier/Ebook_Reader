package com.example.ebook_reader.ui.reader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ebook_reader.data.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<ReaderUiState>(ReaderUiState.Loading)
    val uiState: StateFlow<ReaderUiState> = _uiState

    private var renderer: PdfRenderer? = null
    private var fileDescriptor: ParcelFileDescriptor? = null
    private val bitmapCache = PageBitmapCache()

    private val _isNightMode = MutableStateFlow(false)
    val isNightMode: StateFlow<Boolean> = _isNightMode

    fun toggleNightMode() {
        _isNightMode.value = !_isNightMode.value
    }


    fun openBook(context: Context, bookId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val book = repository.getBookById(bookId) ?: return@launch
            val file = File(book.filePath)

            fileDescriptor =
                ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            renderer = PdfRenderer(fileDescriptor!!)

            _uiState.value = ReaderUiState.Ready(
                totalPages = renderer!!.pageCount,
                currentPage = book.currentPage,
            )
        }
    }

    fun renderPage(pageIndex: Int): Bitmap? {
        bitmapCache.get(pageIndex)?.let { return it }

        val pdfRenderer = renderer ?: return null
        if (pageIndex !in 0 until pdfRenderer.pageCount) return null

        val page = pdfRenderer.openPage(pageIndex)

        val targetWidth = 1080
        val scale = targetWidth.toFloat() / page.width
        val targetHeight = (page.height * scale).toInt()

        val bitmap = Bitmap.createBitmap(
            targetWidth,
            targetHeight,
            Bitmap.Config.ARGB_8888
        )

        page.render(
            bitmap,
            null,
            null,
            PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
        )
        page.close()

        bitmapCache.put(pageIndex, bitmap)
        return bitmap
    }
    fun updateCurrentPage(bookId: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val book = repository.getBookById(bookId) ?: return@launch

            if (book.currentPage != page) {
                val progress =
                    if (book.totalPages > 0)
                        (page + 1).toFloat() / book.totalPages
                    else 0f

                repository.updateBook(
                    book.copy(
                        currentPage = page,
                        progress = progress,
                        lastReadDate = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    override fun onCleared() {
        renderer?.close()
        fileDescriptor?.close()
        super.onCleared()
    }

}