package com.example.ebook_reader.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlin.math.roundToInt

@Composable
fun BookCard(title: String, progress: Float, coverPath: String?, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by
            animateFloatAsState(targetValue = if (isPressed) 0.95f else 1f, label = "card_scale")

    Card(
            modifier =
                    Modifier.fillMaxWidth().scale(scale).clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                            ) { onClick() },
            shape = RoundedCornerShape(16.dp),
            elevation =
                    CardDefaults.cardElevation(defaultElevation = 6.dp, pressedElevation = 2.dp),
            colors =
                    CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
    ) {
        Column {
            // Cover Image
            Box(
                    modifier =
                            Modifier.fillMaxWidth()
                                    .aspectRatio(0.7f)
                                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                    .background(
                                            Brush.verticalGradient(
                                                    colors =
                                                            listOf(
                                                                    MaterialTheme.colorScheme
                                                                            .primaryContainer.copy(
                                                                            alpha = 0.3f
                                                                    ),
                                                                    MaterialTheme.colorScheme
                                                                            .secondaryContainer
                                                                            .copy(alpha = 0.3f)
                                                            )
                                            )
                                    ),
                    contentAlignment = Alignment.Center
            ) {
                if (coverPath != null) {
                    AsyncImage(
                            model = coverPath,
                            contentDescription = title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                    )
                } else {
                    // Elegant placeholder
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                                imageVector = Icons.Outlined.MenuBook,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                                text = "PDF",
                                style = MaterialTheme.typography.labelLarge,
                                color =
                                        MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                alpha = 0.6f
                                        ),
                                fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Book Info
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                        text = title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Progress section
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                ) {
                    LinearProgressIndicator(
                            progress = progress,
                            modifier =
                                    Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                            text = "${(progress * 100).roundToInt()}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
