package com.dsagent.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = LightBlue,
    onPrimary = White,
    secondary = LightBlueVariant,
    background = White,
    surface = White,
    onBackground = DarkText,
    onSurface = DarkText,
    surfaceVariant = LightGray,
    outline = GrayBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = LightBlue,
    onPrimary = White,
    secondary = LightBlueVariant,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = DarkTextLight,
    onSurface = DarkTextLight,
    surfaceVariant = DarkCard,
    outline = DarkBorder
)

@Composable
fun DSAgentTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
