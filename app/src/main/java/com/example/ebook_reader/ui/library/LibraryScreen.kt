package com.example.ebook_reader.ui.library

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ebook_reader.ui.components.BookCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(onBookClick: (String) -> Unit, viewModel: LibraryViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    // Adaptive grid columns based on screen width
    val gridColumns =
            when {
                screenWidthDp >= 900 -> 4 // Large tablets/landscape
                screenWidthDp >= 600 -> 3 // Small tablets
                else -> 2 // Phones
            }

    val pdfPickerLauncher =
            rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.OpenMultipleDocuments()
            ) { uris: List<Uri> -> viewModel.importPdfs(context, uris) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scrollBehavior =
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                LargeTopAppBar(
                        title = {
                            Text(
                                    text = "My Library",
                                    style = MaterialTheme.typography.headlineMedium
                            )
                        },
                        scrollBehavior = scrollBehavior,
                        colors =
                                TopAppBarDefaults.largeTopAppBarColors(
                                        containerColor = MaterialTheme.colorScheme.surface,
                                        scrolledContainerColor = MaterialTheme.colorScheme.surface
                                )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                        onClick = { pdfPickerLauncher.launch(arrayOf("application/pdf")) },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add books",
                            modifier = Modifier.size(24.dp)
                    )
                }
            }
    ) { paddingValues ->
        when (uiState) {
            LibraryUiState.Loading -> {
                Box(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }
            }
            LibraryUiState.Empty -> {
                Box(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        contentAlignment = Alignment.Center
                ) { EmptyLibraryState() }
            }
            is LibraryUiState.Success -> {
                val books = (uiState as LibraryUiState.Success).books

                LazyVerticalGrid(
                        columns = GridCells.Fixed(gridColumns),
                        contentPadding =
                                PaddingValues(
                                        start = 16.dp,
                                        end = 16.dp,
                                        top = 8.dp,
                                        bottom = 88.dp // Extra padding for FAB
                                ),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
                        modifier = Modifier.padding(paddingValues)
                ) {
                    items(books, key = { it.id }) { book ->
                        AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
                            BookCard(
                                    title = book.title,
                                    progress = book.progress,
                                    coverPath = book.coverImagePath,
                                    onClick = { onBookClick(book.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyLibraryState() {
    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
    ) {
        Icon(
                imageVector = Icons.Outlined.MenuBook,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
                text = "Your Library is Empty",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
                text = "Start building your collection by adding PDF books",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
                text = "Tap the + button to begin",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
        )
    }
}
