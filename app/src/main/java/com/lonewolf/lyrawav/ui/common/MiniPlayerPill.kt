package com.lonewolf.lyrawav.ui.common

import androidx.compose.animation.*
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

    // Controla entrada/saída animada do mini player
    AnimatedVisibility(
        visible = showPlayer,
        enter = slideInVertically { it } + fadeIn(),
        exit = slideOutVertically { it } + fadeOut()
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth(0.92f)
                .height(64.dp)
                // Gestos: swipe horizontal (next/prev) e vertical (dismiss/expand)
                .pointerInput(Unit) {
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
                // Tap simples abre o player completo
                .clickable { onPillClick() },
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp).copy(alpha = 0.9f),
            shape = RoundedCornerShape(32.dp),
            shadowElevation = 8.dp,
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Placeholder da capa / avatar do canal
                Box(
                    Modifier.size(44.dp).clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                )

                Spacer(Modifier.width(12.dp))

                // Título e artista
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

                // Botão de play / pause
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
        }
    }
}
