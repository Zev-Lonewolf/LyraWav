package com.lonewolf.lyrawav.ui.home.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun FastMusicGrid() {
    val context = LocalContext.current

    // Grade horizontal de 4 linhas
    LazyHorizontalGrid(
        rows = GridCells.Fixed(4),
        modifier = Modifier
            .height(190.dp)
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items(10) {
            FastMusicCard(onClick = {
                Toast.makeText(context, "Em desenvolvimento :)", Toast.LENGTH_SHORT).show()
            })
        }
    }
}

@Composable
fun FastMusicCard(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .width(260.dp)
            .height(42.dp)
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Capa da música
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color.LightGray, RoundedCornerShape(6.dp))
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Título e Artista (Placeholders)
        Column(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(10.dp)
                    .background(Color.LightGray, RoundedCornerShape(3.dp))
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.45f)
                    .height(8.dp)
                    .background(Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(3.dp))
            )
        }

        // Menu de opções
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(18.dp)
        )
    }
}
