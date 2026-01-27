package com.lonewolf.wavvy.ui.player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp

@Composable
fun AlbumCover(progress: Float, screenWidth: Dp) {
    val size = lerp(52.dp, 300.dp, progress)
    val offsetX = lerp(12.dp, (screenWidth - 300.dp) / 2, progress)
    val offsetY = lerp(6.dp, 80.dp, progress)
    val coverRoundness = lerp(26.dp, 16.dp, progress)

    Box(
        modifier = Modifier
            .offset(offsetX, offsetY)
            .size(size)
            .clip(RoundedCornerShape(coverRoundness))
            .background(MaterialTheme.colorScheme.secondaryContainer)
    )
}
