package com.lonewolf.lyrawav.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF0E0E16), // Destaque principal da interface
    onPrimary = Color(0xFFF8F9FA), // Cor do texto ou Ícone do primary
    tertiary = Color(0xFF00B8D4), // Cor de destaque Neon (Cyan ajustado para contraste no branco)
    background = Color(0xFFF8F9FA), // Fundo geral da interface
    onBackground = Color(0xFF0E0E16), // Texto ou Ícone do background
    surface = Color(0xFFFFFFFF), // Fundo de elementos que flutuam
    onSurface = Color(0xFF0E0E16), // Texto ou Ícone do surface
    surfaceVariant = Color(0xFFE0E0E0), // Fundo de elementos secundários
    onSurfaceVariant = Color(0xFF666666), // Texto ou Ícone do surfaceVariant
    primaryContainer = Color(0xFFE0E0E0), // Uma versão menos vibrante da cor primária
    secondaryContainer = Color(0xFFD6D6DB), // Uma versão menos vibrante da cor secundária
    error = Color(0xFFFF0000),     // O fundo do badge (Vermelho Puro)
    onError = Color(0xFFFFFFFF)    // O texto sobre o vermelho (Branco)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF0E0E16), // Destaque principal da interface
    onPrimary = Color(0xFFF8F9FA), // Cor do texto ou Ícone do primary
    tertiary = Color(0xFF00E5FF), // Cor de destaque Neon (Cyan vibrante para o Dark)
    background = Color(0xFF000000), // Fundo geral da interface (Preto absoluto para contraste)
    onBackground = Color(0xFFF8F9FA), // Texto ou Ícone do background
    surface = Color(0xFF121212), // Fundo de elementos que flutuam
    onSurface = Color(0xFFF8F9FA), // Texto ou Ícone do surface
    surfaceVariant = Color(0xFF1A1A24), // Fundo de elementos secundários
    onSurfaceVariant = Color(0xFFB0B0B8), // Texto ou Ícone do surfaceVariant
    primaryContainer = Color(0xFF1A1A24), // Uma versão menos vibrante da cor primária
    secondaryContainer = Color(0xFFB0B0B8), // Uma versão menos vibrante da cor secundária
    error = Color(0xFFFF0000),     // O fundo do badge (Vermelho Puro)
    onError = Color(0xFFFFFFFF)    // O texto sobre o vermelho (Branco))
)

@Composable
fun LiraWavTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}