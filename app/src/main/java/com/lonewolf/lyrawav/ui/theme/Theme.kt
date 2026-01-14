package com.lonewolf.lyrawav.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Cores para o modo claro
private val LightColors = lightColorScheme(
    primary = Color(0xFF0E0E16),
    onPrimary = Color(0xFFF8F9FA),
    background = Color(0xFFF8F9FA),
    onBackground = Color(0xFF0E0E16),
    surface = Color(0xFFE0E0E0),
    onSurface = Color(0xFF0E0E16),
    surfaceVariant = Color(0xFFD6D6D6),
    onSurfaceVariant = Color(0xFF0E0E16),
    primaryContainer = Color(0xFFE0E0E0)
)

// Cores para o modo escuro
private val DarkColors = darkColorScheme(
    primary = Color(0xFF0E0E16),
    onPrimary = Color(0xFFF8F9FA),
    background = Color(0xFF000000),
    onBackground = Color(0xFFF8F9FA),
    surface = Color(0xFF1A1A24),
    onSurface = Color(0xFFF8F9FA),
    primaryContainer = Color(0xFF1A1A24),
)

@Composable
fun LiraWavTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Escolha entre tema claro ou escuro
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
