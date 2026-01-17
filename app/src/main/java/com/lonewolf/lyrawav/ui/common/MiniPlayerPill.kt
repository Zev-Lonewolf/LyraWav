package com.lonewolf.lyrawav.ui.common

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lonewolf.lyrawav.R
import com.lonewolf.lyrawav.ui.player.PlayerScreen
import com.lonewolf.lyrawav.ui.theme.Poppins
import kotlin.math.abs

@Composable
fun MiniPlayerPill(
    isExpanded: Boolean,
    showPlayer: Boolean,
    songTitle: String,
    artistName: String = stringResource(R.string.default_artist_name),
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    onDismiss: () -> Unit,
    onPillClick: () -> Unit,
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    onSeek: (Float) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    // Spring base para sincronizar movimentos
    val springSpec = spring<Dp>(dampingRatio = 0.8f, stiffness = 300f)

    // Dimensões da pílula
    val pillWidth by animateFloatAsState(if (isExpanded) 1f else 0.92f, label = "pillWidth")
    val pillHeight by animateDpAsState(
        if (isExpanded) screenHeight else 64.dp,
        springSpec,
        label = "pillHeight"
    )
    val pillCorner by animateDpAsState(if (isExpanded) 0.dp else 32.dp, label = "pillCorner")

    // Capa do álbum
    val coverSize by animateDpAsState(if (isExpanded) 300.dp else 44.dp, springSpec, "coverSize")
    val coverRoundness by animateDpAsState(if (isExpanded) 16.dp else 22.dp, label = "coverRound")
    val coverOffsetX by animateDpAsState(
        if (isExpanded) (screenWidth - 300.dp) / 2 else 12.dp,
        springSpec,
        "coverX"
    )
    val coverOffsetY by animateDpAsState(
        if (isExpanded) 100.dp else 10.dp,
        springSpec,
        "coverY"
    )

    // Texto
    val textOffsetX by animateDpAsState(if (isExpanded) 32.dp else 68.dp, springSpec, "textX")
    val textOffsetY by animateDpAsState(if (isExpanded) 430.dp else 12.dp, springSpec, "textY")
    val titleFontSize by animateFloatAsState(if (isExpanded) 24f else 14f, label = "titleSize")

    // Botão play/pause mini
    val buttonSize by animateDpAsState(if (isExpanded) 72.dp else 40.dp, springSpec, "btnSize")
    val iconSize by animateDpAsState(if (isExpanded) 32.dp else 24.dp, springSpec, "iconSize")
    val buttonOffsetX by animateDpAsState(
        if (isExpanded) (screenWidth - 72.dp) / 2 else (screenWidth * 0.92f) - 52.dp,
        springSpec,
        "btnX"
    )
    val buttonOffsetY by animateDpAsState(
        if (isExpanded) screenHeight - 150.dp else 12.dp,
        springSpec,
        "btnY"
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(pillWidth)
                .height(pillHeight)
                .pointerInput(isExpanded) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val (x, y) = dragAmount

                        if (isExpanded) {
                            // Quando expandido: arrastar para baixo minimiza
                            if (y > 30) { // Limite para detectar arraste para baixo
                                onPillClick() // Minimiza o player
                            }
                        } else {
                            // Quando minimizado: gestos originais
                            when {
                                abs(x) > abs(y) && x > 30 -> onNext()
                                abs(x) > abs(y) && x < -30 -> onPrevious()
                                y > 30 -> onDismiss()
                                y < -30 -> onPillClick()
                            }
                        }
                    }
                }
                .clickable {
                    // Clique normal só funciona quando minimizado
                    if (!isExpanded) onPillClick()
                },
            color = MaterialTheme.colorScheme
                .surfaceColorAtElevation(3.dp)
                .copy(alpha = if (isExpanded) 1f else 0.95f),
            shape = RoundedCornerShape(pillCorner),
            shadowElevation = if (isExpanded) 0.dp else 8.dp,
            border = if (isExpanded) null else BorderStroke(
                0.5.dp,
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
        ) {
            Box(Modifier.fillMaxSize()) {

                // Minimizar
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 40.dp, start = 16.dp)
                ) {
                    IconButton(onClick = onPillClick) {
                        Icon(Icons.Default.KeyboardArrowDown, null, Modifier.size(32.dp))
                    }
                }

                // Capa
                Box(
                    modifier = Modifier
                        .offset(coverOffsetX, coverOffsetY)
                        .size(coverSize)
                        .clip(RoundedCornerShape(coverRoundness))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                )

                // Título / Artista
                Column(
                    modifier = Modifier
                        .offset(textOffsetX, textOffsetY)
                        .width(if (isExpanded) screenWidth - 64.dp else 200.dp),
                    horizontalAlignment = if (isExpanded) Alignment.CenterHorizontally else Alignment.Start
                ) {
                    Text(
                        songTitle,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = titleFontSize.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = if (isExpanded) TextAlign.Center else TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        artistName,
                        fontSize = if (isExpanded) 16.sp else 11.sp,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontFamily = Poppins,
                        textAlign = if (isExpanded) TextAlign.Center else TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Player expandido
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn(
                        animationSpec = tween(400, delayMillis = 100)
                    ) + scaleIn(
                        initialScale = 0.9f
                    ),
                    exit = fadeOut(tween(200)),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 80.dp)
                ) {
                    PlayerScreen(
                        isPlaying = isPlaying,
                        currentPosition = 0L,
                        duration = 0L,
                        onPlayPauseToggle = onPlayPauseClick,
                        onNext = onNext,
                        onPrevious = onPrevious,
                        onSeek = onSeek,
                        isVisible = isExpanded
                    )
                }

                // Botão mini
                AnimatedVisibility(
                    visible = !isExpanded,
                    enter = fadeIn(),
                    exit = fadeOut(tween(150)),
                    modifier = Modifier.offset(buttonOffsetX, buttonOffsetY)
                ) {
                    IconButton(
                        onClick = onPlayPauseClick,
                        modifier = Modifier
                            .size(buttonSize)
                            .background(
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                                CircleShape
                            )
                    ) {
                        Icon(
                            if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = stringResource(R.string.cd_play_pause),
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(iconSize)
                        )
                    }
                }
            }
        }
    }
}
