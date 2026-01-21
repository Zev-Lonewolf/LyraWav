package com.lonewolf.lyrawav.ui.common

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import com.lonewolf.lyrawav.ui.theme.Poppins
import kotlin.math.abs

@Composable
fun MiniPlayerPill(
    isExpanded: Boolean,
    songTitle: String,
    artistName: String = stringResource(R.string.default_artist_name),
    onPillClick: () -> Unit,
    onDismiss: () -> Unit,
    onNext: () -> Unit = {},
    onPrevious: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val config = LocalConfiguration.current
    val screenHeight = config.screenHeightDp.dp
    val screenWidth = config.screenWidthDp.dp
    val springSpec = spring<Dp>(dampingRatio = 0.8f, stiffness = 300f)

    val pillWidth by animateFloatAsState(if (isExpanded) 1f else 0.92f)
    val pillCorner by animateDpAsState(if (isExpanded) 0.dp else 32.dp)

    // Animações do mini player (só visíveis quando NÃO expandido)
    val coverSize by animateDpAsState(if (isExpanded) 0.dp else 52.dp, springSpec)
    val coverRoundness by animateDpAsState(if (isExpanded) 0.dp else 26.dp)
    val coverOffsetX by animateDpAsState(if (isExpanded) 0.dp else 12.dp, springSpec)
    val coverOffsetY by animateDpAsState(if (isExpanded) 0.dp else 6.dp, springSpec)

    val textOffsetX by animateDpAsState(if (isExpanded) 0.dp else 76.dp, springSpec)
    val textOffsetY by animateDpAsState(if (isExpanded) 0.dp else 12.dp, springSpec)
    val titleFontSize by animateFloatAsState(if (isExpanded) 0f else 14f)

    val buttonSize by animateDpAsState(if (isExpanded) 0.dp else 40.dp, springSpec)
    val iconSize by animateDpAsState(if (isExpanded) 0.dp else 24.dp, springSpec)
    val buttonOffsetX by animateDpAsState(
        if (isExpanded) 0.dp else (screenWidth * 0.92f) - 52.dp,
        springSpec
    )
    val buttonOffsetY by animateDpAsState(if (isExpanded) 0.dp else 12.dp, springSpec)

    val miniPlayerHeight = 64.dp

    Box(
        modifier = if (isExpanded) {
            Modifier.fillMaxSize()
        } else {
            modifier
                .fillMaxSize()
                .navigationBarsPadding()
        },
        contentAlignment = if (isExpanded) Alignment.TopCenter else Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .then(if (isExpanded) Modifier.fillMaxSize() else Modifier
                    .fillMaxWidth(pillWidth)
                    .height(miniPlayerHeight))
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
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp).copy(alpha = if (isExpanded) 1f else 0.95f),
            shape = RoundedCornerShape(pillCorner),
            shadowElevation = if (isExpanded) 0.dp else 8.dp,
            border = if (isExpanded) null else BorderStroke(0.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
        ) {
            Box(Modifier.fillMaxSize()) {

                // === TELA EXPANDIDA (PLAYER COMPLETO) ===
                if (isExpanded) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .systemBarsPadding() // Respeita status bar E navigation bar
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Botão de minimizar
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                IconButton(onClick = onPillClick) {
                                    Icon(
                                        Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Minimizar",
                                        modifier = Modifier.size(32.dp),
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(32.dp))

                            // Aqui você vai colocar o conteúdo do player expandido:
                            // - Capa grande do álbum
                            // - Título e artista
                            // - Barra de progresso
                            // - Controles (anterior, play/pause, próximo)
                            // - Botões extras (favorito, shuffle, repeat)
                            // - etc.

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Player completo aqui",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }

                // === MINI PLAYER (PÍLULA) ===
                // Só aparece quando NÃO está expandido
                AnimatedVisibility(
                    visible = !isExpanded,
                    enter = fadeIn(),
                    exit = fadeOut(tween(200))
                ) {
                    Box(Modifier.fillMaxSize()) {
                        // Capa do álbum
                        Box(
                            modifier = Modifier
                                .offset(coverOffsetX, coverOffsetY)
                                .size(coverSize)
                                .clip(RoundedCornerShape(coverRoundness))
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                        )

                        // Título e artista
                        Column(
                            modifier = Modifier
                                .offset(textOffsetX, textOffsetY)
                                .width(200.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                songTitle,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = titleFontSize.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = Poppins,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text(
                                artistName,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.tertiary,
                                fontFamily = Poppins,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Ícone de play
                        Box(
                            modifier = Modifier
                                .offset(buttonOffsetX, buttonOffsetY)
                                .size(buttonSize)
                                .background(
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
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
}
