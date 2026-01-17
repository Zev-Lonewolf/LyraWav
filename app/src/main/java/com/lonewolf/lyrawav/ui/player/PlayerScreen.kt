package com.lonewolf.lyrawav.ui.player

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lonewolf.lyrawav.ui.player.components.WaveProgressBar
import com.lonewolf.lyrawav.ui.player.components.PlayerControls
import java.util.concurrent.TimeUnit

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
    // Calcula o progresso (0.0 a 1.0)
    val progress = if (duration > 0) {
        currentPosition.toFloat() / duration.toFloat()
    } else {
        // Permite mover mesmo sem duração
        currentPosition.toFloat() / 100000f
    }

    // Intensidade da onda
    val waveIntensity = 1.0f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // WaveProgressBar
        WaveProgressBar(
            progress = progress,
            isPlaying = isPlaying,
            waveIntensity = waveIntensity,
            onProgressChange = { newProgress ->
                onSeek(newProgress)
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tempo atual / Tempo restante
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatTime(currentPosition),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Text(
                text = "-${formatTime(if (duration > 0) duration - currentPosition else 0L)}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Controles animados
        PlayerControls(
            isPlaying = isPlaying,
            onPlayPauseToggle = onPlayPauseToggle,
            onNext = onNext,
            onPrevious = onPrevious,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Função auxiliar para formatar o tempo
private fun formatTime(milliseconds: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds) - TimeUnit.MINUTES.toSeconds(minutes)
    return String.format("%d:%02d", minutes, seconds)
}
