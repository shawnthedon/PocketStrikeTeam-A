package com.pocketstrike.app.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldAccent,
    onPrimary = DarkBase,
    primaryContainer = DarkGlass,
    onPrimaryContainer = TextPrimaryDark,
    secondary = CyanAccent,
    onSecondary = DarkBase,
    secondaryContainer = DarkGlass,
    onSecondaryContainer = TextPrimaryDark,
    tertiary = VioletAccent,
    background = DarkBase,
    onBackground = TextPrimaryDark,
    surface = DarkBaseAlt,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkGlass,
    onSurfaceVariant = TextSecondaryDark,
    outline = DarkGlassBorder,
    error = StatusError,
    onError = TextPrimaryDark
)

@Composable
fun PocketStrikeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
