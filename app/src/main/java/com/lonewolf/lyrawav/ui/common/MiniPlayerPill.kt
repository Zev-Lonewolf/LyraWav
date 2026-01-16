package com.lonewolf.lyrawav.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lonewolf.lyrawav.R
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
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    // Mola padrão para tudo se mover junto
    val springSpec = spring<androidx.compose.ui.unit.Dp>(dampingRatio = 0.8f, stiffness = 300f)

    // Tamanho e Forma da Pílula Principal
    val pillWidth by animateFloatAsState(if (isExpanded) 1f else 0.92f, label = "width")
    val pillHeight by animateDpAsState(
        if (isExpanded) screenHeight else 64.dp,
        springSpec,
        label = "height"
    )
    val pillCorner by animateDpAsState(if (isExpanded) 0.dp else 32.dp, label = "corner")

    // CAPA DO ÁLBUM
    val coverSize by animateDpAsState(
        if (isExpanded) 300.dp else 44.dp,
        springSpec,
        label = "coverSize"
    )
    val coverRoundness by animateDpAsState(if (isExpanded) 16.dp else 22.dp, label = "coverRound")
    // Posição X: No mini é 12dp (esquerda). No expandido é centralizado.
    val coverOffsetX by animateDpAsState(
        targetValue = if (isExpanded) (screenWidth - 300.dp) / 2 else 12.dp,
        animationSpec = springSpec, label = "coverX"
    )
    // Posição Y: No mini é centralizado verticalmente (10dp). No expandido desce para 100dp.
    val coverOffsetY by animateDpAsState(
        targetValue = if (isExpanded) 100.dp else 10.dp,
        animationSpec = springSpec, label = "coverY"
    )

    // Posição X: No mini fica depois da capa (68dp). No expandido fica quase na borda (32dp).
    val textOffsetX by animateDpAsState(
        targetValue = if (isExpanded) 32.dp else 68.dp,
        animationSpec = springSpec, label = "textX"
    )
    // Posição Y: No mini alinhado ao topo relativo (12dp). No expandido vai para baixo da capa (420dp).
    val textOffsetY by animateDpAsState(
        targetValue = if (isExpanded) 430.dp else 12.dp,
        animationSpec = springSpec, label = "textY"
    )
    // Tamanho da fonte do título
    val titleFontSize by animateFloatAsState(if (isExpanded) 24f else 14f, label = "fontSize")

    // BOTÃO PLAY/PAUSE
    val buttonSize by animateDpAsState(
        if (isExpanded) 72.dp else 40.dp,
        springSpec,
        label = "btnSize"
    )
    val iconSize by animateDpAsState(
        if (isExpanded) 32.dp else 24.dp,
        springSpec,
        label = "iconSize"
    )
    // Posição X: No mini fica na direita (Screen - margem). No expandido centraliza.
    val buttonOffsetX by animateDpAsState(
        targetValue = if (isExpanded) (screenWidth - 72.dp) / 2 else (screenWidth * 0.92f) - 52.dp,
        animationSpec = springSpec, label = "btnX"
    )
    // Posição Y: No mini 12dp. No expandido vai lá pro fundo.
    val buttonOffsetY by animateDpAsState(
        targetValue = if (isExpanded) screenHeight - 150.dp else 12.dp,
        animationSpec = springSpec, label = "btnY"
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
                    if (!isExpanded) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val (x, y) = dragAmount
                            if (abs(x) > abs(y)) {
                                if (x > 30) onNext() else if (x < -30) onPrevious()
                            } else {
                                if (y > 30) onDismiss() else if (y < -30) onPillClick()
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
            // Usamos um Box gigante para mover os elementos livremente com Offset
            Box(modifier = Modifier.fillMaxSize()) {

                // BOTÃO DE MINIMIZAR
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.TopStart)
                        .padding(top = 40.dp, start = 16.dp)
                ) {
                    IconButton(onClick = onPillClick) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Minimizar",
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // CAPA DO ÁLBUM
                Box(
                    modifier = Modifier
                        .offset(x = coverOffsetX, y = coverOffsetY)
                        .size(coverSize)
                        .clip(RoundedCornerShape(coverRoundness))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                )

                // TEXTOS (Título e Artista)
                Column(
                    modifier = Modifier
                        .offset(x = textOffsetX, y = textOffsetY)
                        .width(if (isExpanded) screenWidth - 64.dp else 200.dp), // Limita largura no modo expandido
                    horizontalAlignment = if (isExpanded) Alignment.CenterHorizontally else Alignment.Start
                ) {
                    Text(
                        text = songTitle,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = Poppins,
                        fontSize = titleFontSize.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = if (isExpanded) TextAlign.Center else TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )

                    val artistSize = if (isExpanded) 16.sp else 11.sp
                    Text(
                        text = artistName,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontFamily = Poppins,
                        fontSize = artistSize,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = if (isExpanded) TextAlign.Center else TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // BOTÃO PLAY/PAUSE
                IconButton(
                    onClick = onPlayPauseClick,
                    modifier = Modifier
                        .offset(x = buttonOffsetX, y = buttonOffsetY)
                        .size(buttonSize)
                        .background(
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = stringResource(R.string.cd_play_pause),
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }
        }
    }
}
