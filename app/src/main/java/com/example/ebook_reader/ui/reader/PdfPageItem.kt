package com.example.ebook_reader.ui.reader

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp

@Composable
fun PdfPageItem(
        pageIndex: Int,
        viewModel: ReaderViewModel,
        pageHeight: Dp,
        scale: Float,
        offset: Offset,
        isCurrentPage: Boolean
) {
        var bitmap by remember { mutableStateOf<Bitmap?>(null) }

        LaunchedEffect(pageIndex) { bitmap = viewModel.renderPage(pageIndex) }

        bitmap?.let {
                Box(modifier = Modifier.fillMaxWidth().height(pageHeight)) {
                        Image(
                                bitmap = it.asImageBitmap(),
                                contentDescription = "Page ${pageIndex + 1}",
                                contentScale = ContentScale.FillWidth,
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .height(pageHeight)
                                                .graphicsLayer(
                                                        scaleX = scale,
                                                        scaleY = scale,
                                                        translationX = offset.x,
                                                        translationY = offset.y
                                                )
                        )
                }
        }
                ?: Spacer(modifier = Modifier.fillMaxWidth().height(pageHeight))
}
