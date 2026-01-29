package com.example.ebook_reader.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ReaderTopBar(
        title: String,
        isNightMode: Boolean,
        onToggleNightMode: () -> Unit,
        pageInfo: String,
        onBack: () -> Unit
) {
        Box(
                modifier =
                        Modifier.fillMaxWidth()
                                .background(Color.Transparent)
                                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
                // Left side - Page indicator with icon
                Row(
                        modifier = Modifier.align(Alignment.CenterStart),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Pages",
                                modifier = Modifier.size(16.dp),
                                tint =
                                        if (isNightMode) Color.White.copy(alpha = 0.7f)
                                        else Color.Black.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                                text = pageInfo,
                                style = MaterialTheme.typography.bodySmall,
                                color =
                                        if (isNightMode) Color.White.copy(alpha = 0.7f)
                                        else Color.Black.copy(alpha = 0.7f),
                                fontSize = 13.sp
                        )
                }

                // Center - Chapter title
                Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isNightMode) Color.White else Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 16.sp
                )

                // Right side - Close button
                IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterEnd)) {
                        Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Close",
                                tint = if (isNightMode) Color.White else Color.Black
                        )
                }
        }
}
