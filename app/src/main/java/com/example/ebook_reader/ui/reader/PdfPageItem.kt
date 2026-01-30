package com.example.ebook_reader.ui.reader

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PdfPageItem(
        pageIndex: Int,
        viewModel: ReaderViewModel,
        pageHeight: Dp,
        scale: Float,
        offset: Offset,
        isCurrentPage: Boolean,
        isNightMode: Boolean = false
) {
        var bitmap by remember { mutableStateOf<Bitmap?>(null) }

        LaunchedEffect(pageIndex) { bitmap = viewModel.renderPage(pageIndex) }

        // Page container - fills full available space with minimal padding
        Box(
                modifier =
                        Modifier.fillMaxWidth()
                                .height(pageHeight)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
        ) {
                bitmap?.let {
                        // Clean book page with subtle shadow
                        Box(
                                modifier =
                                        Modifier.fillMaxSize()
                                                .shadow(
                                                        elevation = 4.dp,
                                                        shape = RoundedCornerShape(4.dp),
                                                        ambientColor =
                                                                Color.Black.copy(alpha = 0.2f),
                                                        spotColor = Color.Black.copy(alpha = 0.2f)
                                                )
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(
                                                        if (isNightMode) Color(0xFF2D2A26)
                                                        else Color(0xFFFAF6F0)
                                                )
                                                .graphicsLayer(
                                                        scaleX = scale,
                                                        scaleY = scale,
                                                        translationX = offset.x,
                                                        translationY = offset.y
                                                )
                        ) {
                                Image(
                                        bitmap = it.asImageBitmap(),
                                        contentDescription = "Page ${pageIndex + 1}",
                                        contentScale = ContentScale.Fit,
                                        modifier = Modifier.fillMaxSize().padding(8.dp)
                                )
                        }
                }
                        ?: Box(
                                modifier =
                                        Modifier.fillMaxSize()
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(
                                                        if (isNightMode) Color(0xFF2D2A26)
                                                        else Color(0xFFFAF6F0)
                                                )
                        )
        }
}
