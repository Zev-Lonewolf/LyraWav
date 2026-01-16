package com.lonewolf.lyrawav.ui.player.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.sin

@Composable
fun WaveProgressBar(
    progress: Float,
    wavePhase: Float,
    waveIntensity: Float,
    onProgressChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val activeColor = colorScheme.tertiary
    val trackColor = colorScheme.onSurfaceVariant.copy(alpha = 0.25f)

    // Anima o progresso
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing),
        label = "SmoothProgress"
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val newProgress = (offset.x / size.width).coerceIn(0f, 1f)
                    onProgressChange(newProgress)
                }
            }
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    change.consume()
                    val newProgress = (change.position.x / size.width).coerceIn(0f, 1f)
                    onProgressChange(newProgress)
                }
            }
    ) {
        val width = size.width
        val centerY = size.height / 2
        val waveLength = 60.dp.toPx()
        val amplitude = 3.dp.toPx() * waveIntensity

        // Progresso usado no desenho
        val playedWidth = width * animatedProgress.coerceIn(0f, 1f)
        val step = 3f

        // Onda ativa
        var x = 0f
        while (x < playedWidth) {
            val nextX = (x + step).coerceAtMost(playedWidth)
            val p1 = (x / waveLength) * 2 * Math.PI + wavePhase
            val p2 = (nextX / waveLength) * 2 * Math.PI + wavePhase

            drawLine(
                color = activeColor,
                start = Offset(x, (centerY + sin(p1) * amplitude).toFloat()),
                end = Offset(nextX, (centerY + sin(p2) * amplitude).toFloat()),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
            x += step
        }

        // Linha de fundo
        drawLine(
            color = trackColor,
            start = Offset(playedWidth, centerY),
            end = Offset(width, centerY),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Thumb
        drawCircle(
            color = activeColor.copy(alpha = 0.2f),
            radius = 12.dp.toPx(),
            center = Offset(playedWidth, centerY)
        )
        drawCircle(
            color = activeColor,
            radius = 7.dp.toPx(),
            center = Offset(playedWidth, centerY)
        )
    }
}
