package com.lonewolf.lyrawav.ui.common

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.lonewolf.lyrawav.ui.player.components.AlbumCover
import com.lonewolf.lyrawav.ui.player.components.ExpandedPlayerContent
import com.lonewolf.lyrawav.ui.player.components.MiniPlayerContent
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

fun lerpFloat(start: Float, stop: Float, fraction: Float): Float = start + fraction * (stop - start)

@Composable
fun PlayerContainer(
    isExpanded: Boolean,
    songTitle: String,
    artistName: String,
    onPillClick: () -> Unit,
    onDismiss: () -> Unit,
    onProgressUpdate: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val config = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeight = config.screenHeightDp.dp
    val screenWidth = config.screenWidthDp.dp
    val scope = rememberCoroutineScope()

    val bottomMargin = 110.dp
    val maxOffset = with(density) { (screenHeight - 64.dp - bottomMargin).toPx() }

    val containerAlpha = remember { Animatable(0f) }
    val offsetY = remember { Animatable(maxOffset + 150f) }

    val progress = (1f - (offsetY.value / maxOffset)).coerceIn(0f, 1f)
    LaunchedEffect(progress) { onProgressUpdate(progress) }

    val currentWidthFraction = lerpFloat(0.92f, 1f, progress)
    val currentCorner = lerp(32.dp, 0.dp, progress)
    val currentHeight = lerp(64.dp, screenHeight + bottomMargin, progress)

    // Entrada com fade suave
    LaunchedEffect(Unit) {
        launch { containerAlpha.animateTo(1f, tween(500)) }
        launch { offsetY.animateTo(maxOffset, spring(0.82f, 350f)) }
    }

    LaunchedEffect(isExpanded) {
        offsetY.animateTo(if (isExpanded) 0f else maxOffset, spring(0.85f, 400f))
    }

    Box(
        modifier = modifier
            .alpha(containerAlpha.value),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(0, offsetY.value.roundToInt()) }
                .fillMaxWidth(currentWidthFraction)
                .height(currentHeight)
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        scope.launch {
                            if (!(offsetY.value >= maxOffset && delta > 0)) {
                                offsetY.snapTo((offsetY.value + delta).coerceIn(-50f, maxOffset + 50f))
                            }
                        }
                    },
                    onDragStopped = { velocity ->
                        scope.launch {
                            if (velocity > 600 && offsetY.value >= maxOffset) {
                                launch { containerAlpha.animateTo(0f, tween(200)) }
                                offsetY.animateTo(maxOffset + 300f, tween(300))
                                onDismiss()
                            } else {
                                val target = if (velocity < -700 || offsetY.value < maxOffset * 0.45f) 0f else maxOffset
                                offsetY.animateTo(target, spring(0.85f, 400f))
                                if (target == 0f && !isExpanded) onPillClick()
                                else if (target == maxOffset && isExpanded) onPillClick()
                            }
                        }
                    }
                )
                .clickable(enabled = progress < 0.05f) { onPillClick() }
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                    .copy(alpha = lerpFloat(0.7f, 1f, (progress * 1.2f).coerceIn(0f, 1f))),
                shape = RoundedCornerShape(currentCorner),
                shadowElevation = lerp(8.dp, 0.dp, progress)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .then(
                            if (progress > 0.5f) {
                                // Quando expandido, aplica padding do sistema
                                Modifier
                                    .statusBarsPadding()
                                    .navigationBarsPadding()
                            } else {
                                Modifier
                            }
                        )
                ) {
                    AlbumCover(progress = progress, screenWidth = screenWidth)

                    Box(Modifier.alpha((1f - progress * 6f).coerceIn(0f, 1f))) {
                        MiniPlayerContent(
                            isExpanded = false,
                            songTitle = songTitle,
                            artistName = artistName,
                            screenWidth = screenWidth,
                            springSpec = spring()
                        )
                    }

                    val expandedAlpha = ((progress - 0.4f) * 1.8f).coerceIn(0f, 1f)
                    Box(Modifier.alpha(expandedAlpha)) {
                        ExpandedPlayerContent(
                            isExpanded = progress > 0.4f,
                            songTitle = songTitle,
                            artistName = artistName,
                            onMinimize = onPillClick
                        )
                    }
                }
            }
        }
    }
}
