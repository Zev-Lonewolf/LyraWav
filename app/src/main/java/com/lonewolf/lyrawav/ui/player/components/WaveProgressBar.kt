package com.lonewolf.lyrawav.ui.player.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.sin

@Composable
fun WaveProgressBar(
    progress: Float,
    isPlaying: Boolean,
    waveIntensity: Float,
    onProgressChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    // Cores
    val colors = MaterialTheme.colorScheme
    val activeColor = colors.tertiary
    val trackColor = colors.onSurfaceVariant.copy(alpha = 0.25f)

    // Density para converter dp → px
    val density = LocalDensity.current
    val baseAmplitudePx = remember(waveIntensity) {
        with(density) { 3.dp.toPx() * waveIntensity }
    }

    // Fase da onda
    val infiniteTransition = rememberInfiniteTransition(label = "WavePhase")
    val phase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PhaseAnim"
    )

    // Pausa congela a onda
    val currentPhase = if (isPlaying) phase else 0f

    // Progresso suave
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "ProgressAnim"
    )

    // Amplitude animada (zera ao pausar)
    val animatedAmplitude by animateFloatAsState(
        targetValue = if (isPlaying) baseAmplitudePx else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "AmplitudeAnim"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            // Tap
            .pointerInput(Unit) {
                detectTapGestures {
                    onProgressChange((it.x / size.width).coerceIn(0f, 1f))
                }
            }
            // Drag
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    onProgressChange(
                        (change.position.x / size.width).coerceIn(0f, 1f)
                    )
                }
            }
    ) {
        val width = size.width
        val centerY = size.height / 2
        val waveLength = 60.dp.toPx()
        val playedWidth = width * animatedProgress
        val step = 3f

        // Onda ativa
        var x = 0f
        while (x < playedWidth) {
            val nextX = (x + step).coerceAtMost(playedWidth)

            val p1 = (x / waveLength) * 2 * Math.PI + currentPhase
            val p2 = (nextX / waveLength) * 2 * Math.PI + currentPhase

            drawLine(
                color = activeColor,
                start = Offset(x, centerY + sin(p1).toFloat() * animatedAmplitude),
                end = Offset(nextX, centerY + sin(p2).toFloat() * animatedAmplitude),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
            x += step
        }

        // Track
        drawLine(
            color = trackColor,
            start = Offset(playedWidth, centerY),
            end = Offset(width, centerY),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Halo
        drawCircle(
            color = activeColor.copy(alpha = 0.2f),
            radius = 12.dp.toPx(),
            center = Offset(playedWidth, centerY)
        )

        // Thumb
        drawCircle(
            color = activeColor,
            radius = 7.dp.toPx(),
            center = Offset(playedWidth, centerY)
        )
    }
}
