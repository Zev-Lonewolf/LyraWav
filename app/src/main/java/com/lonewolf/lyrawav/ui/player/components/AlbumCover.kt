package com.lonewolf.lyrawav.ui.player.components

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AlbumCover(
    isExpanded: Boolean,
    screenWidth: Dp,
    springSpec: AnimationSpec<Dp>,
    modifier: Modifier = Modifier
) {
    // Animações da capa
    val coverSize by animateDpAsState(
        targetValue = if (isExpanded) 300.dp else 52.dp,
        animationSpec = springSpec
    )
    val coverRoundness by animateDpAsState(
        targetValue = if (isExpanded) 16.dp else 26.dp,
        animationSpec = springSpec
    )
    val coverOffsetX by animateDpAsState(
        targetValue = if (isExpanded) (screenWidth - 300.dp) / 2 else 12.dp,
        animationSpec = springSpec
    )
    val coverOffsetY by animateDpAsState(
        targetValue = if (isExpanded) 80.dp else 6.dp,
        animationSpec = springSpec
    )

    Box(
        modifier = modifier
            .offset(coverOffsetX, coverOffsetY)
            .size(coverSize)
            .clip(RoundedCornerShape(coverRoundness))
            .background(MaterialTheme.colorScheme.secondaryContainer)
    )
}
