package com.example.ebook_reader.ui.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ebook_reader.ui.components.ReaderTopBar

@Composable
fun ReaderScreen(bookId: String, viewModel: ReaderViewModel = hiltViewModel(), onBack: () -> Unit) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    var showUi by remember { mutableStateOf(true) }
    val isNightMode by viewModel.isNightMode.collectAsState()

    LaunchedEffect(bookId) { viewModel.openBook(context, bookId) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }.collect { page ->
            viewModel.updateCurrentPage(bookId, page)
        }
    }

    Box(
            modifier =
                    Modifier.fillMaxSize()
                            .background(if (isNightMode) Color(0xFF1C1917) else Color(0xFFFAF6F0))
    ) {
        // Reader Container
        Box(
                modifier =
                        Modifier.fillMaxSize().pointerInput(Unit) {
                            detectTapGestures { showUi = !showUi }
                        }
        ) {
            when (uiState) {
                ReaderUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
                is ReaderUiState.Ready -> {
                    val state = uiState as ReaderUiState.Ready
                    val totalPages = state.totalPages
                    val currentPage = listState.firstVisibleItemIndex

                    LazyColumn(
                            state = listState,
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(totalPages) { index ->
                            PdfPageItem(pageIndex = index, viewModel = viewModel)
                        }
                    }

                    // Top Bar
                    if (showUi) {
                        ReaderTopBar(
                                title = "Reading",
                                isNightMode = isNightMode,
                                onToggleNightMode = { viewModel.toggleNightMode() },
                                pageInfo = "${currentPage + 1} / $totalPages",
                                onBack = onBack
                        )
                    }
                }
            }
        }
    }
}
