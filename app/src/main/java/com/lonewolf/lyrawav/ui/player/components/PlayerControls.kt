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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
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

    // Interações dos botões
    val mainInteraction = remember { MutableInteractionSource() }
    val nextInteraction = remember { MutableInteractionSource() }
    val prevInteraction = remember { MutableInteractionSource() }

    val isMainPressed by mainInteraction.collectIsPressedAsState()
    val isNextPressed by nextInteraction.collectIsPressedAsState()
    val isPrevPressed by prevInteraction.collectIsPressedAsState()

    // Escala botão central
    val mainScale by animateFloatAsState(
        targetValue = if (isMainPressed) 0.88f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium),
        label = "MainScale"
    )

    // Feedback botões laterais
    val nextScale by animateFloatAsState(
        targetValue = if (isNextPressed) 0.85f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh)
    )
    val prevScale by animateFloatAsState(
        targetValue = if (isPrevPressed) 0.85f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh)
    )

    val nextOffsetX by animateFloatAsState(
        targetValue = if (isNextPressed) 8f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium)
    )
    val prevOffsetX by animateFloatAsState(
        targetValue = if (isPrevPressed) -8f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium)
    )

    // Rotação do ícone
    val iconRotation by animateFloatAsState(
        targetValue = if (isPlaying) 180f else 0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMediumLow),
        label = "IconRotation"
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Anterior
        IconButton(
            onClick = onPrevious,
            interactionSource = prevInteraction,
            modifier = Modifier
                .size(56.dp)
                .scale(prevScale)
                .graphicsLayer { translationX = prevOffsetX }
        ) {
            Icon(Icons.Rounded.SkipPrevious, null, tint = colors.onBackground, modifier = Modifier.size(42.dp))
        }

        // Play / Pause
        Box(
            modifier = Modifier
                .size(80.dp)
                .scale(mainScale)
                .shadow(if (isMainPressed) 4.dp else 12.dp, CircleShape)
                .background(colors.onBackground, CircleShape)
                .clip(CircleShape)
                .clickable(
                    interactionSource = mainInteraction,
                    indication = ripple(bounded = true, color = colors.background),
                    onClick = onPlayPauseToggle
                ),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = isPlaying,
                transitionSpec = {
                    (scaleIn(
                        initialScale = 0.6f,
                        animationSpec = spring(0.6f, Spring.StiffnessMedium)
                    ) + fadeIn(tween(150)))
                        .togetherWith(
                            scaleOut(
                                targetScale = 0.6f,
                                animationSpec = tween(100)
                            ) + fadeOut(tween(100))
                        )
                },
                label = "PlayPauseMorph"
            ) { playing ->
                Icon(
                    imageVector = if (playing) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                    contentDescription = null,
                    tint = colors.background,
                    modifier = Modifier
                        .size(44.dp)
                        .graphicsLayer { rotationZ = iconRotation }
                )
            }
        }

        // Próximo
        IconButton(
            onClick = onNext,
            interactionSource = nextInteraction,
            modifier = Modifier
                .size(56.dp)
                .scale(nextScale)
                .graphicsLayer { translationX = nextOffsetX }
        ) {
            Icon(Icons.Rounded.SkipNext, null, tint = colors.onBackground, modifier = Modifier.size(42.dp))
        }
    }
}
