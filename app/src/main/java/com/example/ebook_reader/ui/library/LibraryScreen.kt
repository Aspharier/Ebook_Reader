package com.example.ebook_reader.ui.library

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ebook_reader.ui.components.CategoryRow
import com.example.ebook_reader.ui.components.CategorySelectionDialog

@Composable
fun LibraryScreen(onBookClick: (String) -> Unit, viewModel: LibraryViewModel = hiltViewModel()) {
        val context = LocalContext.current
        var showCategoryDialog by remember { mutableStateOf(false) }
        var pendingUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

        val pdfPickerLauncher =
                rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.OpenMultipleDocuments()
                ) { uris: List<Uri> ->
                        if (uris.isNotEmpty()) {
                                pendingUris = uris
                                showCategoryDialog = true
                        }
                }
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        // Category Selection Dialog
        if (showCategoryDialog) {
                CategorySelectionDialog(
                        onCategorySelected = { category ->
                                viewModel.importPdfs(context, pendingUris, category.displayName)
                                showCategoryDialog = false
                                pendingUris = emptyList()
                        },
                        onDismiss = {
                                showCategoryDialog = false
                                pendingUris = emptyList()
                        }
                )
        }

        Scaffold(
                topBar = {
                        // Custom centered top bar
                        Column(
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .systemBarsPadding()
                                                .background(MaterialTheme.colorScheme.surface),
                                horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                                Text(
                                        text = "My Favourite",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                        text = "BOOKS",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        letterSpacing = 2.sp
                                )
                        }
                }
        ) { paddingValues ->
                when (uiState) {
                        LibraryUiState.Loading -> {
                                Box(
                                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                                        contentAlignment = Alignment.Center
                                ) {
                                        CircularProgressIndicator(
                                                color = MaterialTheme.colorScheme.primary
                                        )
                                }
                        }
                        LibraryUiState.Empty -> {
                                Box(
                                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                                        contentAlignment = Alignment.Center
                                ) {
                                        EmptyLibraryState(
                                                onAddBooks = {
                                                        pdfPickerLauncher.launch(
                                                                arrayOf("application/pdf")
                                                        )
                                                }
                                        )
                                }
                        }
                        is LibraryUiState.Success -> {
                                val successState = uiState as LibraryUiState.Success
                                val categorizedBooks = successState.categorizedBooks

                                Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                                        // Scrollable content
                                        LazyColumn(
                                                modifier = Modifier.weight(1f),
                                                verticalArrangement = Arrangement.spacedBy(24.dp)
                                        ) {
                                                // Display each category with books
                                                categorizedBooks.forEach { (category, books) ->
                                                        item(key = category) {
                                                                CategoryRow(
                                                                        categoryName = category,
                                                                        books = books,
                                                                        onBookClick = onBookClick
                                                                )
                                                        }
                                                }

                                                item { Spacer(modifier = Modifier.height(80.dp)) }
                                        }

                                        // Add Books Button at bottom
                                        Box(
                                                modifier =
                                                        Modifier.fillMaxWidth()
                                                                .background(
                                                                        MaterialTheme.colorScheme
                                                                                .surface
                                                                )
                                                                .padding(20.dp),
                                                contentAlignment = Alignment.Center
                                        ) {
                                                Button(
                                                        onClick = {
                                                                pdfPickerLauncher.launch(
                                                                        arrayOf("application/pdf")
                                                                )
                                                        },
                                                        shape = RoundedCornerShape(24.dp),
                                                        colors =
                                                                ButtonDefaults.buttonColors(
                                                                        containerColor =
                                                                                Color.Black,
                                                                        contentColor = Color.White
                                                                ),
                                                ) {
                                                        //
                                                        //              Icon(
                                                        //
                                                        //                      imageVector =
                                                        // Icons.Filled.Add,
                                                        //
                                                        //                      contentDescription =
                                                        // null,
                                                        //
                                                        //                      modifier =
                                                        // Modifier.size(20.dp)
                                                        //
                                                        //              )
                                                        //
                                                        //              Spacer(modifier =
                                                        // Modifier.size(8.dp))
                                                        Text(
                                                                text = "Add Books",
                                                                style =
                                                                        MaterialTheme.typography
                                                                                .titleMedium,
                                                                fontWeight = FontWeight.SemiBold,
                                                                modifier =
                                                                        Modifier.padding(
                                                                                top = 10.dp,
                                                                                bottom = 10.dp
                                                                        )
                                                        )
                                                }
                                        }
                                }
                        }
                }
        }
}

@Composable
fun EmptyLibraryState(onAddBooks: () -> Unit) {
        Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(32.dp)
        ) {
                Icon(
                        imageVector = Icons.Outlined.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                        text = "Tap + to open your first PDF",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Add Books Button
                Button(
                        onClick = onAddBooks,
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                        colors =
                                ButtonDefaults.buttonColors(
                                        containerColor = Color.Black,
                                        contentColor = Color.White
                                )
                ) {
                        //                        Icon(
                        //                                imageVector = Icons.Filled.Add,
                        //                                contentDescription = null,
                        //                                modifier = Modifier.size(20.dp)
                        //                        )
                        //                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                                text = "Add Books",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                        )
                }
        }
}
