package com.lonewolf.lyrawav.ui.player

import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lonewolf.lyrawav.ui.theme.Poppins
import com.lonewolf.lyrawav.ui.player.components.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlayerScreen(
    title: String = "Música Desconhecida",
    artist: String = "Artista",
    imageUrl: String = "",
    isPlaying: Boolean = false,
    currentPosition: Long = 0L,
    duration: Long = 0L,
    onPlayPauseToggle: () -> Unit = {},
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    onSeek: (Long) -> Unit = {},
    onMinimize: () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme
    val isDark = isSystemInDarkTheme()

    // Seek temporário
    var draggingProgress by remember { mutableStateOf<Float?>(null) }

    // Animação da onda
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val wavePhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing)),
        label = "phase"
    )

    val waveIntensity by animateFloatAsState(
        targetValue = if (isPlaying) 1f else 0f,
        animationSpec = tween(800),
        label = "intensity"
    )

    Box(modifier = Modifier.fillMaxSize().background(colorScheme.background)) {

        // Fundo com blur
        if (imageUrl.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(context).data(imageUrl).crossfade(true).build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .scale(1.5f)
                    .blur(80.dp)
                    .graphicsLayer(alpha = if (isDark) 0.55f else 0.3f)
            )
        }

        // Gradiente de leitura
        Box(
            modifier = Modifier.fillMaxSize().background(
                Brush.verticalGradient(
                    0.4f to Color.Transparent,
                    1.0f to colorScheme.background.copy(alpha = 0.95f)
                )
            )
        )

        Column(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.12f))
            PlayerAlbumArt(imageUrl = imageUrl)
            Spacer(modifier = Modifier.weight(0.15f))

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            ) {
                Text(
                    text = title,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = colorScheme.onBackground,
                    modifier = Modifier.basicMarquee()
                )

                Text(
                    text = artist,
                    fontFamily = Poppins,
                    fontSize = 18.sp,
                    color = colorScheme.onBackground.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(35.dp))

                // Progresso
                val currentProgress =
                    draggingProgress
                        ?: if (duration > 0) currentPosition.toFloat() / duration.toFloat()
                        else 0f

                WaveProgressBar(
                    progress = currentProgress,
                    wavePhase = wavePhase,
                    waveIntensity = waveIntensity,
                    onProgressChange = { newProgress ->
                        draggingProgress = newProgress
                        onSeek((newProgress * duration).toLong())
                    }
                )

                // Reset do drag
                LaunchedEffect(currentPosition) {
                    draggingProgress = null
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val displayPos =
                        if (draggingProgress != null)
                            (draggingProgress!! * duration).toLong()
                        else currentPosition

                    Text(
                        formatTime(displayPos),
                        color = colorScheme.onBackground.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )

                    val rem = if (duration >= displayPos) duration - displayPos else 0L
                    Text(
                        "-${formatTime(rem)}",
                        color = colorScheme.onBackground.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                PlayerControls(
                    isPlaying = isPlaying,
                    onPlayPauseToggle = onPlayPauseToggle,
                    onNext = onNext,
                    onPrevious = onPrevious
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            PlayerFooter()
        }
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}
