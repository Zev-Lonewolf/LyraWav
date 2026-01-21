package com.lonewolf.lyrawav.ui.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lonewolf.lyrawav.ui.player.components.AlbumCover
import com.lonewolf.lyrawav.ui.player.components.ExpandedPlayerContent
import com.lonewolf.lyrawav.ui.player.components.MiniPlayerContent
import kotlin.math.abs

@Composable
fun PlayerContainer(
    isExpanded: Boolean,
    songTitle: String,
    artistName: String,
    onPillClick: () -> Unit,
    onDismiss: () -> Unit,
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp.dp
    val springSpec = spring<Dp>(dampingRatio = 0.75f, stiffness = 250f)

    // Animações do container
    val pillWidth by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0.92f,
        animationSpec = tween(durationMillis = 350)
    )
    val pillCorner by animateDpAsState(
        targetValue = if (isExpanded) 0.dp else 32.dp,
        animationSpec = tween(durationMillis = 350)
    )

    val miniPlayerHeight = 64.dp

    Box(
        modifier = if (isExpanded) {
            Modifier.fillMaxSize()
        } else {
            modifier
        },
        contentAlignment = if (isExpanded) Alignment.TopCenter else Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .then(
                    if (isExpanded) Modifier.fillMaxSize()
                    else Modifier
                        .fillMaxWidth(pillWidth)
                        .height(miniPlayerHeight)
                )
                .pointerInput(isExpanded) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val (x, y) = dragAmount
                        if (isExpanded) {
                            if (y > 30) onPillClick()
                        } else {
                            when {
                                abs(x) > abs(y) && x > 30 -> onNext()
                                abs(x) > abs(y) && x < -30 -> onPrevious()
                                y > 30 -> onDismiss()
                                y < -30 -> onPillClick()
                            }
                        }
                    }
                }
                .clickable { if (!isExpanded) onPillClick() },
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                .copy(alpha = if (isExpanded) 1f else 0.95f),
            shape = RoundedCornerShape(pillCorner),
            shadowElevation = if (isExpanded) 0.dp else 8.dp,
            border = if (isExpanded) null else BorderStroke(
                0.5.dp,
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
        ) {
            Box(Modifier.fillMaxSize()) {
                // Capa do álbum (anima entre mini e expandido)
                AlbumCover(
                    isExpanded = isExpanded,
                    screenWidth = screenWidth,
                    springSpec = springSpec
                )

                // Conteúdo do mini player
                MiniPlayerContent(
                    isExpanded = isExpanded,
                    songTitle = songTitle,
                    artistName = artistName,
                    screenWidth = screenWidth,
                    springSpec = springSpec
                )

                // Conteúdo do player expandido
                ExpandedPlayerContent(
                    isExpanded = isExpanded,
                    songTitle = songTitle,
                    artistName = artistName,
                    onMinimize = onPillClick
                )
            }
        }
    }
}
