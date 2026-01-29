package com.example.ebook_reader.ui.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
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

    // Initialize list state with saved reading position
    val initialPage =
            if (uiState is ReaderUiState.Ready) {
                (uiState as ReaderUiState.Ready).currentPage
            } else 0

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialPage)
    var showUi by remember { mutableStateOf(true) }
    val isNightMode by viewModel.isNightMode.collectAsState()

    // Shared zoom state for entire document
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val transformState = rememberTransformableState { zoomChange, panChange, _ ->
        scale = (scale * zoomChange).coerceIn(1f, 4f)

        // Allow panning when zoomed
        if (scale > 1f) {
            offset = offset + panChange
        } else {
            offset = Offset.Zero
        }
    }

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
                            .statusBarsPadding()
    ) {
        // Reader Container
        Box(
                modifier =
                        Modifier.fillMaxSize().transformable(state = transformState).pointerInput(
                                        Unit
                                ) {
                            detectTapGestures(
                                    onTap = { showUi = !showUi },
                                    onDoubleTap = {
                                        // Reset zoom on double tap
                                        scale = 1f
                                        offset = Offset.Zero
                                    }
                            )
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
                            verticalArrangement = Arrangement.spacedBy(0.dp) // No gap between pages
                    ) {
                        items(totalPages) { index ->
                            PdfPageItem(
                                    pageIndex = index,
                                    viewModel = viewModel,
                                    scale = scale,
                                    offset = offset
                            )
                        }
                    }

                    // Top Bar
                    if (showUi) {
                        ReaderTopBar(
                                title = "Chapter 40",
                                isNightMode = isNightMode,
                                onToggleNightMode = { viewModel.toggleNightMode() },
                                pageInfo = "${currentPage + 1} / $totalPages",
                                onBack = onBack
                        )
                    }

                    // Bottom Navigation Bar
                    if (showUi) {
                        Box(
                                modifier =
                                        Modifier.align(Alignment.BottomCenter)
                                                .fillMaxWidth()
                                                .background(Color.Transparent)
                                                .navigationBarsPadding()
                                                .padding(bottom = 16.dp)
                        ) {
                            //                            Row(
                            //                                    modifier =
                            //
                            // Modifier.align(Alignment.BottomCenter)
                            //
                            // .padding(horizontal = 16.dp),
                            //                                    horizontalArrangement =
                            // Arrangement.Center
                            //                            ) {
                            //                                IconButton(onClick = { /* TODO: Show
                            // table of contents */}) {
                            //                                    Icon(
                            //                                            imageVector =
                            // Icons.Filled.Menu,
                            //                                            contentDescription =
                            // "Table of Contents",
                            //                                            tint = if (isNightMode)
                            // Color.White else Color.Black,
                            //                                            modifier =
                            // Modifier.size(28.dp)
                            //                                    )
                            //                                }
                            //                            }
                        }
                    }
                }
            }
        }
    }
}
