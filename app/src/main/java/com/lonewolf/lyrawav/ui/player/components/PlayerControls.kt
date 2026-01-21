package com.lonewolf.lyrawav.ui.player.components

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun PlayerControls(
    isPlaying: Boolean,
    onPlayPauseToggle: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val haptic = LocalHapticFeedback.current

    // Fontes de interação
    val mainInteraction = remember { MutableInteractionSource() }
    val nextInteraction = remember { MutableInteractionSource() }
    val prevInteraction = remember { MutableInteractionSource() }

    val isMainPressed by mainInteraction.collectIsPressedAsState()
    val isNextPressed by nextInteraction.collectIsPressedAsState()
    val isPrevPressed by prevInteraction.collectIsPressedAsState()

    // Escala do botão principal
    val mainScale by animateFloatAsState(
        targetValue = if (isMainPressed) 0.88f else 1f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessLow),
        label = "MainScale"
    )

    // Rotação play/pause
    val rotation by animateFloatAsState(
        targetValue = if (isPlaying) 180f else 0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow),
        label = "Rotation"
    )

    // Animações laterais
    val sideSpring = spring<Float>(dampingRatio = 0.45f, stiffness = 300f)
    val nextScale by animateFloatAsState(if (isNextPressed) 0.75f else 1f, sideSpring)
    val prevScale by animateFloatAsState(if (isPrevPressed) 0.75f else 1f, sideSpring)
    val nextOffsetX by animateFloatAsState(if (isNextPressed) 15f else 0f, sideSpring)
    val prevOffsetX by animateFloatAsState(if (isPrevPressed) -15f else 0f, sideSpring)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Anterior
        IconButton(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onPrevious()
            },
            interactionSource = prevInteraction,
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer {
                    scaleX = prevScale
                    scaleY = prevScale
                    translationX = prevOffsetX
                }
        ) {
            Icon(
                imageVector = Icons.Rounded.SkipPrevious,
                contentDescription = "Anterior",
                tint = colors.onBackground,
                modifier = Modifier.size(40.dp)
            )
        }

        // Play / Pause
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(90.dp)
                .graphicsLayer {
                    scaleX = mainScale
                    scaleY = mainScale
                    rotationZ = rotation
                }
                .clip(CircleShape)
                .clickable(
                    interactionSource = mainInteraction,
                    indication = ripple(
                        bounded = true,
                        color = colors.onBackground
                    ),
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onPlayPauseToggle()
                    }
                )
        ) {
            AnimatedContent(
                targetState = isPlaying,
                label = "PlayPause"
            ) { playing ->
                Icon(
                    imageVector = if (playing) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = colors.onBackground,
                    modifier = Modifier.size(52.dp)
                )
            }
        }

        // Próximo
        IconButton(
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onNext()
            },
            interactionSource = nextInteraction,
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer {
                    scaleX = nextScale
                    scaleY = nextScale
                    translationX = nextOffsetX
                }
        ) {
            Icon(
                imageVector = Icons.Rounded.SkipNext,
                contentDescription = "Próximo",
                tint = colors.onBackground,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}
