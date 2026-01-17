package com.lonewolf.lyrawav.ui.player

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lonewolf.lyrawav.ui.player.components.PlayerControls

@Composable
fun PlayerScreen(
    isPlaying: Boolean,
    currentPosition: Long,
    duration: Long,
    onPlayPauseToggle: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    // Visibilidade animada do player (entrada/saída)
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
