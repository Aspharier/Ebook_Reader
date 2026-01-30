package com.example.ebook_reader.ui.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ebook_reader.ui.components.ReaderTopBar

@Composable
fun ReaderScreen(bookId: String, viewModel: ReaderViewModel = hiltViewModel(), onBack: () -> Unit) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current

    // Set pageHeight to full screen height to cover the whole screen
    val pageHeight = configuration.screenHeightDp.dp

    // Initialize list state with saved reading position
    val initialPage =
            if (uiState is ReaderUiState.Ready) {
                (uiState as ReaderUiState.Ready).currentPage
            } else 0

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialPage)
    var showUi by remember { mutableStateOf(true) }
    val isNightMode by viewModel.isNightMode.collectAsState()

    // Track current visible page for page-specific zoom
    val currentVisiblePage by remember { derivedStateOf { listState.firstVisibleItemIndex } }

    // Page-specific zoom state (only affects current page)
    var zoomedPageIndex by remember { mutableStateOf(-1) }
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val transformState = rememberTransformableState { zoomChange, panChange, _ ->
        // Set the zoomed page to current visible page
        if (zoomedPageIndex != currentVisiblePage) {
            zoomedPageIndex = currentVisiblePage
            scale = 1f
            offset = Offset.Zero
        }

        scale = (scale * zoomChange).coerceIn(1f, 4f)

        // Allow panning when zoomed
        if (scale > 1f) {
            offset = offset + panChange
        } else {
            offset = Offset.Zero
        }
    }

    // Reset zoom when page changes
    LaunchedEffect(currentVisiblePage) {
        if (currentVisiblePage != zoomedPageIndex) {
            scale = 1f
            offset = Offset.Zero
            zoomedPageIndex = -1
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
                                        zoomedPageIndex = -1
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

                    // Snap fling behavior for smooth page snapping
                    val snapBehavior = rememberSnapFlingBehavior(lazyListState = listState)

                    LazyColumn(
                            state = listState,
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(0.dp),
                            flingBehavior = snapBehavior
                    ) {
                        items(totalPages) { index ->
                            PdfPageItem(
                                    pageIndex = index,
                                    viewModel = viewModel,
                                    pageHeight = pageHeight,
                                    scale = if (index == zoomedPageIndex) scale else 1f,
                                    offset = if (index == zoomedPageIndex) offset else Offset.Zero,
                                    isCurrentPage = index == currentVisiblePage
                            )
                        }
                    }

                    // Top Bar - Overlays the content and handles status bar padding
                    if (showUi) {
                        Box(modifier = Modifier.statusBarsPadding()) {
                            ReaderTopBar(
                                    title = "Chapter 40",
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
}
