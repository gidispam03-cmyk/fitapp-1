package com.fitcoach.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = FitGreen,
    secondary = FitOrange,
    background = FitBackground,
    surface = FitSurface,
    onSurface = FitOnSurface
)

private val DarkColors = darkColorScheme(
    primary = FitGreenLight,
    secondary = FitOrange
)

@Composable
fun FitCoachTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = MaterialTheme.typography,
        content = content
    )
}
