package com.lonewolf.lyrawav.ui.player.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun PlayerAlbumArt(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val backgroundColor = MaterialTheme.colorScheme.background

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .drawWithContent {
                drawContent()

                // Gradiente de recorte
                drawRect(
                    brush = Brush.verticalGradient(
                        0.0f to Color.Transparent,
                        0.15f to backgroundColor,
                        0.85f to backgroundColor,
                        1.0f to Color.Transparent
                    ),
                    blendMode = BlendMode.DstIn
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl.isEmpty()) {
            AlbumPlaceholder()
        } else {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Capa do Álbum",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
                loading = { AlbumPlaceholder() },
                error = { AlbumPlaceholder() }
            )
        }
    }
}

@Composable
private fun AlbumPlaceholder() {
    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        colors.surfaceVariant,
                        colors.background
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.MusicNote,
            contentDescription = null,
            modifier = Modifier
                .size(85.dp)
                .graphicsLayer { alpha = 0.4f },
            tint = colors.onSurfaceVariant
        )
    }
}
