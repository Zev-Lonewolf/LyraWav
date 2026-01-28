package com.lonewolf.wavvy.ui.player.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

    // Cálculos de posição e tamanho do botão principal
    val startX = screenWidth * 0.92f - 56.dp
    val startY = 12.dp
    val endX = (screenWidth - 90.dp) / 2
    val endY = screenHeight * 0.75f

    val currentX = lerp(startX, endX, progress)
    val currentY = lerp(startY, endY, progress)
    val currentSize = lerp(40.dp, 90.dp, progress)
    val currentIconSize = lerp(24.dp, 52.dp, progress)

    // Animação de rotação do ícone play/pause
    val rotation by animateFloatAsState(
        targetValue = if (isPlaying) 180f else 0f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "rotation"
    )

    Box(modifier = modifier.fillMaxSize()) {

        // Skip buttons com formato Metrolist usando weight
        val sideAlpha = ((progress - 0.6f) / 0.25f).coerceIn(0f, 1f)

        if (progress > 0.55f) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = currentY + (currentSize / 2) - 34.dp)
                    .alpha(sideAlpha)
                    .padding(horizontal = 40.dp)
            ) {
                val previousInteraction = remember { MutableInteractionSource() }
                val nextInteraction = remember { MutableInteractionSource() }
                val playPauseInteraction = remember { MutableInteractionSource() }

                val isPreviousPressed by previousInteraction.collectIsPressedAsState()
                val isNextPressed by nextInteraction.collectIsPressedAsState()
                val isPlayPausePressed by playPauseInteraction.collectIsPressedAsState()

                // Animação de peso dos botões (estilo Metrolist)
                val previousWeight by animateFloatAsState(
                    targetValue = if (isPreviousPressed) 0.65f
                    else if (isPlayPausePressed) 0.35f
                    else 0.45f,
                    animationSpec = spring(
                        dampingRatio = 0.6f,
                        stiffness = 500f
                    ),
                    label = "previousWeight"
                )

                val playPauseWeight by animateFloatAsState(
                    targetValue = if (isPlayPausePressed) 1.9f
                    else if (isPreviousPressed || isNextPressed) 1.1f
                    else 1.3f,
                    animationSpec = spring(
                        dampingRatio = 0.6f,
                        stiffness = 500f
                    ),
                    label = "playPauseWeight"
                )

                val nextWeight by animateFloatAsState(
                    targetValue = if (isNextPressed) 0.65f
                    else if (isPlayPausePressed) 0.35f
                    else 0.45f,
                    animationSpec = spring(
                        dampingRatio = 0.6f,
                        stiffness = 500f
                    ),
                    label = "nextWeight"
                )

                // Botão Anterior
                FilledIconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onPrevious()
                    },
                    interactionSource = previousInteraction,
                    shape = RoundedCornerShape(50),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = colors.onSurface.copy(alpha = 0.08f),
                        contentColor = colors.onSurface
                    ),
                    modifier = Modifier
                        .height(68.dp)
                        .weight(previousWeight)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.SkipPrevious,
                        contentDescription = "Anterior",
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Botão Play/Pause (versão expandida com weight)
                FilledIconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onPlayPauseToggle()
                    },
                    interactionSource = playPauseInteraction,
                    shape = RoundedCornerShape(50),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = colors.onSurface.copy(alpha = 0.08f),
                        contentColor = colors.onSurface
                    ),
                    modifier = Modifier
                        .height(68.dp)
                        .weight(playPauseWeight)
                ) {
                    // Ícone com rotação
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isPlaying) "Pausar" else "Play",
                        modifier = Modifier
                            .size(32.dp)
                            .graphicsLayer { rotationZ = rotation }
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Botão Próximo
                FilledIconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onNext()
                    },
                    interactionSource = nextInteraction,
                    shape = RoundedCornerShape(50),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = colors.onSurface.copy(alpha = 0.08f),
                        contentColor = colors.onSurface
                    ),
                    modifier = Modifier
                        .height(68.dp)
                        .weight(nextWeight)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.SkipNext,
                        contentDescription = "Próximo",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        // Play/Pause na posição minimizada (quando progress < 0.55f)
        if (progress < 0.6f) {
            val mainInteraction = remember { MutableInteractionSource() }
            val isMainPressed by mainInteraction.collectIsPressedAsState()

            val mainScale by animateFloatAsState(
                targetValue = if (isMainPressed) 0.88f else 1f,
                animationSpec = spring(dampingRatio = 0.4f),
                label = "mainScale"
            )

            FilledIconButton(
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onPlayPauseToggle()
                },
                interactionSource = mainInteraction,
                shape = RoundedCornerShape(50),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = colors.onSurface.copy(
                        alpha = if (progress > 0.5f) 0.18f else 0.08f
                    ),
                    contentColor = colors.onSurface
                ),
                modifier = Modifier
                    .offset(x = currentX, y = currentY)
                    .size(currentSize)
                    .graphicsLayer {
                        scaleX = mainScale
                        scaleY = mainScale
                    }
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = if (isPlaying) "Pausar" else "Play",
                    modifier = Modifier
                        .size(currentIconSize)
                        .graphicsLayer { rotationZ = rotation }
                )
            }
        }
    }
}
