package com.lonewolf.wavvy.ui.player.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayerControls(
    progress: Float,
    isPlaying: Boolean,
    onPlayPauseToggle: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    screenWidth: Dp,
    screenHeight: Dp,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val haptic = LocalHapticFeedback.current

    val mainInteraction = remember { MutableInteractionSource() }
    val isMainPressed by mainInteraction.collectIsPressedAsState()
    val mainScale by animateFloatAsState(
        targetValue = if (isMainPressed) 0.88f else 1f,
        animationSpec = spring(dampingRatio = 0.4f), label = ""
    )
    val rotation by animateFloatAsState(
        targetValue = if (isPlaying) 180f else 0f,
        animationSpec = spring(dampingRatio = 0.6f), label = ""
    )

    val startX = screenWidth * 0.92f - 56.dp
    val startY = 12.dp
    val endX = (screenWidth - 90.dp) / 2
    val endY = screenHeight * 0.75f

    val currentX = lerp(startX, endX, progress)
    val currentY = lerp(startY, endY, progress)
    val currentSize = lerp(40.dp, 90.dp, progress)
    val currentIconSize = lerp(24.dp, 52.dp, progress)

    Box(modifier = modifier.fillMaxSize()) {

        // Skip buttons com transição de fade suave
        // Começa a aparecer a partir de progress 0.6 e fica totalmente visível em 0.85
        val sideAlpha = ((progress - 0.6f) / 0.25f).coerceIn(0f, 1f)

        if (progress > 0.55f) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = currentY + (currentSize / 2) - 32.dp)
                    .alpha(sideAlpha),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onPrevious,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.SkipPrevious,
                        contentDescription = "Anterior",
                        tint = colors.onSurface,
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.width(100.dp))
                IconButton(
                    onClick = onNext,
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.SkipNext,
                        contentDescription = "Próximo",
                        tint = colors.onSurface,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }

        // Play/Pause central
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .offset(x = currentX, y = currentY)
                .size(currentSize)
                .graphicsLayer { scaleX = mainScale; scaleY = mainScale; rotationZ = rotation }
                .clip(CircleShape)
                .background(if (progress > 0.5f) colors.primary else colors.primary.copy(alpha = 0.1f))
                .clickable(
                    interactionSource = mainInteraction,
                    indication = ripple(bounded = true),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onPlayPauseToggle()
                    }
                )
        ) {
            AnimatedContent(targetState = isPlaying, label = "") { playing ->
                Icon(
                    imageVector = if (playing) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (playing) "Pausar" else "Play",
                    tint = if (progress > 0.5f) colors.onPrimary else colors.onSurface,
                    modifier = Modifier.size(currentIconSize)
                )
            }
        }
    }
}
