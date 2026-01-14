package com.lonewolf.lyrawav.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lonewolf.lyrawav.R

@Composable
fun FastMusicGrid(onItemClick: (String) -> Unit) {
    // Grade de músicas
    LazyHorizontalGrid(
        rows = GridCells.Fixed(4),
        modifier = Modifier
            .height(190.dp)
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(10) { index ->
            val musicName = "Music Choice ${index + 1}"
            FastMusicCard(
                title = musicName,
                onClick = { onItemClick(musicName) }
            )
        }
    }
}

@Composable
fun FastMusicCard(title: String, onClick: () -> Unit) {
    // Card individual
    Row(
        modifier = Modifier
            .width(260.dp)
            .height(42.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Capa
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(6.dp))
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Info da música
        Column(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(10.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(3.dp))
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .height(8.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f), RoundedCornerShape(3.dp))
            )
        }

        // Menu
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = stringResource(R.string.cd_more_options),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun FastChoiceCard(onClick: () -> Unit) {
    // Card individual visual
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(72.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable { onClick() }
    )
}
