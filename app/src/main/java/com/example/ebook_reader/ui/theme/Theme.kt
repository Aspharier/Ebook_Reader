package com.example.ebook_reader.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Dark color scheme - Sophisticated night reading theme
private val DarkColorScheme =
        darkColorScheme(
                primary = LightBrown,
                onPrimary = DarkCharcoal,
                primaryContainer = WarmBrown,
                onPrimaryContainer = CreamPaper,
                secondary = MintTeal,
                onSecondary = DarkCharcoal,
                secondaryContainer = DeepTeal,
                onSecondaryContainer = CreamPaper,
                tertiary = PeachGold,
                onTertiary = DarkCharcoal,
                tertiaryContainer = GoldenAmber,
                onTertiaryContainer = CreamPaper,
                background = DarkCharcoal,
                onBackground = CreamWhite,
                surface = DarkCharcoal,
                onSurface = CreamWhite,
                surfaceVariant = WarmGray,
                onSurfaceVariant = CreamPaper,
                outline = Color(0xFF8B7E74),
                outlineVariant = Color(0xFF4E4539)
        )

// Light color scheme - Warm, book-inspired theme
private val LightColorScheme =
        lightColorScheme(
                primary = WarmBrown,
                onPrimary = Color.White,
                primaryContainer = SoftBeige,
                onPrimaryContainer = DarkBrown,
                secondary = DeepTeal,
                onSecondary = Color.White,
                secondaryContainer = Color(0xFFB2DFDB),
                onSecondaryContainer = Color(0xFF00251A),
                tertiary = GoldenAmber,
                onTertiary = Color.White,
                tertiaryContainer = Color(0xFFFFE0B2),
                onTertiaryContainer = Color(0xFF2E1500),
                background = CreamPaper,
                onBackground = DarkBrown,
                surface = CreamPaper,
                onSurface = DarkBrown,
                surfaceVariant = SoftBeige,
                onSurfaceVariant = Color(0xFF4E4539),
                outline = Color(0xFF7D7167),
                outlineVariant = Color(0xFFD0C4B8)
        )

@Composable
fun Ebook_ReaderTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        // Dynamic color is available on Android 12+
        dynamicColor: Boolean = true,
        content: @Composable () -> Unit
) {
    val colorScheme =
            when {
                dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                    val context = LocalContext.current
                    if (darkTheme) dynamicDarkColorScheme(context)
                    else dynamicLightColorScheme(context)
                }
                darkTheme -> DarkColorScheme
                else -> LightColorScheme
            }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
