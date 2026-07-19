package com.dsagent.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = LightBlue,
    onPrimary = White,
    secondary = LightBlueVariant,
    background = White,
    surface = White,
    onBackground = DarkText,
    onSurface = DarkText
)

@Composable
fun DSAgentTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
