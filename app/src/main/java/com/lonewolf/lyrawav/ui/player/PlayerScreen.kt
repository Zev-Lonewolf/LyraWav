package com.lonewolf.lyrawav.ui.player

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lonewolf.lyrawav.ui.player.components.PlayerActionToolbar
import com.lonewolf.lyrawav.ui.player.components.PlayerControls
import com.lonewolf.lyrawav.ui.player.components.WaveProgressBar
import java.util.concurrent.TimeUnit

@Composable
fun PlayerScreen(
    // Playback
    isPlaying: Boolean,
    currentPosition: Long,
    duration: Long,

    // Player actions
    onPlayPauseToggle: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSeek: (Float) -> Unit,

    // UI state (vem de fora!)
    isFavorite: Boolean,
    onFavoriteChange: (Boolean) -> Unit,

    isShuffleActive: Boolean,
    onShuffleChange: (Boolean) -> Unit,

    repeatMode: Int,
    onRepeatChange: (Int) -> Unit,

    modifier: Modifier = Modifier,
) {
    // Progresso
    val progress = if (duration > 0)
        currentPosition.toFloat() / duration.toFloat()
    else
        currentPosition.toFloat() / 100000f

    val waveIntensity = 1f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Controles principais
        PlayerControls(
            isPlaying = isPlaying,
            onPlayPauseToggle = onPlayPauseToggle,
            onNext = onNext,
            onPrevious = onPrevious,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        Spacer(Modifier.height(24.dp))

        // Wave progress
        WaveProgressBar(
            progress = progress,
            isPlaying = isPlaying,
            waveIntensity = waveIntensity,
            onProgressChange = onSeek,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // Tempo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                formatTime(currentPosition),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
            )
            Text(
                "-${formatTime(if (duration > 0) duration - currentPosition else 0L)}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.6f)
            )
        }

        Spacer(Modifier.height(32.dp))

        // Toolbar
        PlayerActionToolbar(
            isFavorite = isFavorite,
            onFavoriteClick = {
                onFavoriteChange(!isFavorite)
            },
            onDownloadClick = { /* Download */ },

            repeatMode = repeatMode,
            onRepeatClick = {
                onRepeatChange((repeatMode + 1) % 3)
            },

            isShuffleActive = isShuffleActive,
            onShuffleClick = {
                onShuffleChange(!isShuffleActive)
            },

            onMoreOptionsClick = { /* Menu */ },
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
