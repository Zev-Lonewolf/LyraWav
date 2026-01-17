package com.lonewolf.lyrawav.ui.player

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lonewolf.lyrawav.ui.player.components.PlayerControls
import com.lonewolf.lyrawav.ui.player.components.WaveProgressBar

@Composable
fun PlayerScreen(
    isPlaying: Boolean,
    currentPosition: Long,
    duration: Long,
    onPlayPauseToggle: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSeek: (Float) -> Unit,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    // 1. Estado local para permitir o movimento livre da barra
    var localProgress by remember { mutableFloatStateOf(0f) }

    // 2. Sincroniza o progresso local com a música real quando ela estiver tocando
    LaunchedEffect(currentPosition, duration) {
        if (duration > 0) {
            localProgress = currentPosition.toFloat() / duration.toFloat()
        }
    }

    // Visibilidade animada do player
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = spring(stiffness = 300f)) +
                slideInVertically(
                    initialOffsetY = { 100 },
                    animationSpec = spring(dampingRatio = 0.6f)
                ),
        exit = fadeOut(animationSpec = spring(stiffness = 400f)) +
                slideOutVertically(targetOffsetY = { 50 })
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Barra de Progresso em Onda
            WaveProgressBar(
                progress = localProgress.coerceIn(0f, 1f),
                // AQUI ESTAVA O ERRO: Trocamos wavePhase por isPlaying
                isPlaying = isPlaying,
                waveIntensity = if (isPlaying) 1f else 0.2f,
                onProgressChange = { newProgress ->
                    localProgress = newProgress
                    onSeek(newProgress)
                },
                modifier = Modifier.fillMaxWidth().height(40.dp)
            )

            // Indicadores de tempo (MM:SS)
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(if (duration > 0) currentPosition else (localProgress * 1000).toLong()),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
                Text(
                    text = formatTime(duration),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Controles principais do player
            PlayerControls(
                isPlaying = isPlaying,
                onPlayPauseToggle = onPlayPauseToggle,
                onNext = onNext,
                onPrevious = onPrevious,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%02d:%02d".format(minutes, seconds)
}
