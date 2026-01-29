package com.example.ebook_reader.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun BookCard(coverPath: String?, progress: Float = 0f, onClick: () -> Unit) {
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed by interactionSource.collectIsPressedAsState()

        val scale by
                animateFloatAsState(
                        targetValue = if (isPressed) 0.95f else 1f,
                        label = "card_scale"
                )

        Column(modifier = Modifier.width(120.dp)) {
                Card(
                        modifier =
                                Modifier.width(120.dp).scale(scale).clickable(
                                                interactionSource = interactionSource,
                                                indication = null
                                        ) { onClick() },
                        shape = RoundedCornerShape(12.dp),
                        elevation =
                                CardDefaults.cardElevation(
                                        defaultElevation = 4.dp,
                                        pressedElevation = 2.dp
                                ),
                        colors =
                                CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                ) {
                        // Cover Image Only
                        Box(
                                modifier =
                                        Modifier.width(120.dp)
                                                .aspectRatio(0.67f)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(
                                                        Brush.verticalGradient(
                                                                colors =
                                                                        listOf(
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .primaryContainer
                                                                                        .copy(
                                                                                                alpha =
                                                                                                        0.3f
                                                                                        ),
                                                                                MaterialTheme
                                                                                        .colorScheme
                                                                                        .secondaryContainer
                                                                                        .copy(
                                                                                                alpha =
                                                                                                        0.3f
                                                                                        )
                                                                        )
                                                        )
                                                ),
                                contentAlignment = Alignment.Center
                        ) {
                                if (coverPath != null) {
                                        AsyncImage(
                                                model = coverPath,
                                                contentDescription = "Book cover",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                        )
                                } else {
                                        // Elegant placeholder
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Icon(
                                                        imageVector = Icons.Outlined.MenuBook,
                                                        contentDescription = null,
                                                        modifier = Modifier.size(40.dp),
                                                        tint =
                                                                MaterialTheme.colorScheme.primary
                                                                        .copy(alpha = 0.5f)
                                                )
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(
                                                        text = "PDF",
                                                        style =
                                                                MaterialTheme.typography
                                                                        .labelMedium,
                                                        color =
                                                                MaterialTheme.colorScheme
                                                                        .onSurfaceVariant.copy(
                                                                        alpha = 0.6f
                                                                ),
                                                        fontWeight = FontWeight.SemiBold
                                                )
                                        }
                                }
                        }
                }

                // Progress Bar
                if (progress > 0f) {
                        Spacer(modifier = Modifier.height(6.dp))
                        androidx.compose.material3.LinearProgressIndicator(
                                progress = progress,
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .height(4.dp)
                                                .clip(RoundedCornerShape(2.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                }
        }
}
