package com.example.ebook_reader.ui.reader

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun PdfPageItem(
    pageIndex: Int,
    viewModel: ReaderViewModel
) {
    var bitmap by remember {
        mutableStateOf<android.graphics.Bitmap?>(null)
    }

    LaunchedEffect(pageIndex) {
        bitmap = viewModel.renderPage(pageIndex)
    }

    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = "Page ${pageIndex + 1}",
            modifier = Modifier.fillMaxWidth()
        )
    } ?: Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}