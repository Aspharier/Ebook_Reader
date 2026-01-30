package com.example.ebook_reader.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ebook_reader.R

@Composable
fun ReaderTopBar(
        title: String,
        isNightMode: Boolean,
        onToggleNightMode: () -> Unit,
        pageInfo: String,
        pagesLeft: String = "",
        onBack: () -> Unit
) {
        Box(
                modifier =
                        Modifier.fillMaxWidth()
                                .background(Color.Transparent)
                                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
                // Left side - Page indicator badge
                Row(
                        modifier = Modifier.align(Alignment.CenterStart),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        // Page count badge with dark background
                        Box(
                                modifier =
                                        Modifier.clip(CircleShape)
                                                .background(
                                                        if (isNightMode)
                                                                Color.White.copy(alpha = 0.15f)
                                                        else Color.Black.copy(alpha = 0.6f)
                                                )
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                                Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                ) {
                                        Icon(
                                                painter = painterResource(id = R.drawable.ic_pages),
                                                contentDescription = "Pages",
                                                modifier = Modifier.size(14.dp),
                                                tint = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                                text = pageInfo,
                                                color = Color.White,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Medium
                                        )
                                }
                        }
                }

                // Center - Pages left info (like "4 pages left in chapter")
                if (pagesLeft.isNotEmpty()) {
                        Text(
                                text = pagesLeft,
                                color =
                                        if (isNightMode) Color.White.copy(alpha = 0.6f)
                                        else Color.Black.copy(alpha = 0.7f),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.align(Alignment.Center)
                        )
                }

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
