package com.lonewolf.lyrawav.ui.common

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
    // Cor de destaque adaptada ao tema
    val accentColor = MaterialTheme.colorScheme.tertiary
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    // Estados animados
    val animatedWidth by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0.92f,
        label = "width"
    )
    val animatedHeight by animateDpAsState(
        targetValue = if (isExpanded) screenHeight else 64.dp,
        // spring para sincronizar com o padding da HomeScreen
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f),
        label = "height"
    )
    val animatedCorner by animateDpAsState(
        targetValue = if (isExpanded) 0.dp else 32.dp,
        label = "corner"
    )

    // Box externo com Alignment.BottomCenter para a pílula subir
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(animatedWidth)
                .height(animatedHeight)
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
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp).copy(alpha = if (isExpanded) 1f else 0.9f),
            shape = RoundedCornerShape(animatedCorner),
            shadowElevation = if (isExpanded) 0.dp else 8.dp,
            border = if (isExpanded) null else BorderStroke(0.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Conteúdo da Pílula (Mini)
                if (!isExpanded) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                        )

                        Spacer(Modifier.width(12.dp))

                        Column(Modifier.weight(1f)) {
                            Text(
                                text = songTitle,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = Poppins,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = artistName,
                                color = accentColor,
                                fontFamily = Poppins,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        IconButton(
                            onClick = onPlayPauseClick,
                            modifier = Modifier
                                .size(40.dp)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f), CircleShape)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = stringResource(R.string.cd_play_pause),
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                } else {
                    // Player Completo
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Modo Expandido",
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Spacer(Modifier.height(24.dp))
                            Button(onClick = onPillClick) {
                                Text("Minimizar")
                            }
                        }
                    }
                }
            }
        }
    }
}
