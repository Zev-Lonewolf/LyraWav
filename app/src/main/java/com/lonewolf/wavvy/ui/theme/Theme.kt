package com.lonewolf.wavvy.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF0E0E16),
    onPrimary = Color(0xFFF8F9FA),
    tertiary = Color(0xFF00B8D4),
    background = Color(0xFFF8F9FA),
    onBackground = Color(0xFF0E0E16),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0E0E16),
    surfaceVariant = Color(0xFFE0E0E0),
    onSurfaceVariant = Color(0xFF666666),
    primaryContainer = Color(0xFFE0E0E0),
    secondaryContainer = Color(0xFFD6D6DB),
    error = Color(0xFFFF0000),
    onError = Color(0xFFFFFFFF),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF0E0E16),
    onPrimary = Color(0xFFF8F9FA),
    tertiary = Color(0xFF00E5FF),
    background = Color(0xFF000000),
    onBackground = Color(0xFFF8F9FA),
    surface = Color(0xFF121212),
    onSurface = Color(0xFFF8F9FA),
    surfaceVariant = Color(0xFF1A1A24),
    onSurfaceVariant = Color(0xFFB0B0B8),
    primaryContainer = Color(0xFF1A1A24),
    secondaryContainer = Color(0xFFB0B0B8),
    error = Color(0xFFFF0000),
    onError = Color(0xFFFFFFFF)
)

// Cores customizadas
object CustomColors {
    val gradientStartLight = Color(0xFF5E35B1)
    val gradientMiddleLight = Color(0xFF7E57C2)
    val gradientEndLight = Color(0xFFAB47BC)

    val gradientStartDark = Color(0xFF1A237E)
    val gradientMiddleDark = Color(0xFF4A148C)
    val gradientEndDark = Color(0xFF880E4F)
}

// Gradientes dos gêneros musicais
object GenreGradients {
    val pop = Brush.linearGradient(listOf(Color(0xFF8E24AA), Color(0xFFBA68C8)))
    val rock = Brush.linearGradient(listOf(Color(0xFF263238), Color(0xFF455A64)))
    val hiphop = Brush.linearGradient(listOf(Color(0xFFBF360C), Color(0xFFFF7043)))
    val electronic = Brush.linearGradient(listOf(Color(0xFF1B5E20), Color(0xFF66BB6A)))
    val indie = Brush.linearGradient(listOf(Color(0xFF283593), Color(0xFF5C6BC0)))
    val lofi = Brush.linearGradient(listOf(Color(0xFF1A237E), Color(0xFF3F51B5)))
    val jazz = Brush.linearGradient(listOf(Color(0xFF3E2723), Color(0xFF795548)))
    val soul = Brush.linearGradient(listOf(Color(0xFF4E342E), Color(0xFFA1887F)))
    val rnb = Brush.linearGradient(listOf(Color(0xFF311B92), Color(0xFF9575CD)))
    val ambient = Brush.linearGradient(listOf(Color(0xFF004D40), Color(0xFF4DB6AC)))
    val metal = Brush.linearGradient(listOf(Color(0xFF000000), Color(0xFF424242)))
    val punk = Brush.linearGradient(listOf(Color(0xFFB71C1C), Color(0xFFD32F2F)))
    val hardrock = Brush.linearGradient(listOf(Color(0xFF212121), Color(0xFF616161)))
    val phonk = Brush.linearGradient(listOf(Color(0xFF004D40), Color(0xFF009688)))
    val trap = Brush.linearGradient(listOf(Color(0xFF263238), Color(0xFF546E7A)))

    val flamenco = Brush.linearGradient(listOf(Color(0xFFD32F2F), Color(0xFFE64A19)))
    val arabic = Brush.linearGradient(listOf(Color(0xFF5D4037), Color(0xFF8D6E63)))
    val greek = Brush.linearGradient(listOf(Color(0xFF0277BD), Color(0xFF4FC3F7)))

    val kpop = Brush.linearGradient(listOf(Color(0xFFD81B60), Color(0xFFF48FB1)))
    val jpop = Brush.linearGradient(listOf(Color(0xFFAD1457), Color(0xFFF06292)))
    val cpop = Brush.linearGradient(listOf(Color(0xFFB71C1C), Color(0xFFFFD600)))
    val hindustani = Brush.linearGradient(listOf(Color(0xFFE65100), Color(0xFFFFB74D)))

    val mpb = Brush.linearGradient(listOf(Color(0xFF1B5E20), Color(0xFF81C784)))
    val funk = Brush.linearGradient(listOf(Color(0xFF4A148C), Color(0xFFAB47BC)))
    val sertanejo = Brush.linearGradient(listOf(Color(0xFF3E2723), Color(0xFFD7CCC8)))
    val pagode = Brush.linearGradient(listOf(Color(0xFFBF360C), Color(0xFFFFAB91)))
    val rapNacional = Brush.linearGradient(listOf(Color(0xFF263238), Color(0xFF90A4AE)))
    val reggaeton = Brush.linearGradient(listOf(Color(0xFFC2185B), Color(0xFFF06292)))

    val afrobeat = Brush.linearGradient(listOf(Color(0xFF1B5E20), Color(0xFFFBC02D)))
    val reggae = Brush.linearGradient(listOf(Color(0xFF388E3C), Color(0xFFFBC02D)))

    val vaporwave = Brush.linearGradient(listOf(Color(0xFF4A148C), Color(0xFFCE93D8)))
    val synthwave = Brush.linearGradient(listOf(Color(0xFF0D47A1), Color(0xFF64B5F6)))
    val citypop = Brush.linearGradient(listOf(Color(0xFF006064), Color(0xFF4DD0E1)))
}

// Cores dos chips de descoberta
object DiscoveryChipColors {
    val trending = Color(0xFF00BFA5)
    val top50 = Color(0xFF6200EE)
    val releases = Color(0xFFFF5252)
    val mixes = Color(0xFFFFAB40)
    val community = Color(0xFF1E88E5)
    val radios = Color(0xFFFDD835)
}

// Extension properties
val MaterialTheme.gradientColors: List<Color>
    @Composable
    @ReadOnlyComposable
    get() = if (isSystemInDarkTheme()) {
        listOf(
            CustomColors.gradientStartDark,
            CustomColors.gradientMiddleDark,
            CustomColors.gradientEndDark
        )
    } else {
        listOf(
            CustomColors.gradientStartLight,
            CustomColors.gradientMiddleLight,
            CustomColors.gradientEndLight
        )
    }

val MaterialTheme.neonAccent: Color
    @Composable
    @ReadOnlyComposable
    get() = colorScheme.tertiary

@Composable
fun WavvyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
