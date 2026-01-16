package com.lonewolf.lyrawav.ui.player.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlayerFooter(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val tint = colors.onBackground.copy(alpha = 0.6f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        // Baixar
        IconButton(onClick = { }) {
            Icon(Icons.Default.Download, null, tint = tint)
        }

        // Adicionar à biblioteca
        IconButton(onClick = { }) {
            Icon(Icons.Default.LibraryAdd, null, tint = tint)
        }

        // Repetir
        IconButton(onClick = { }) {
            Icon(Icons.Default.Repeat, null, tint = tint)
        }

        // Randomizar
        IconButton(onClick = { }) {
            Icon(Icons.Default.Shuffle, null, tint = tint)
        }

        // Mais opções
        IconButton(onClick = { }) {
            Icon(Icons.Default.MoreVert, null, tint = tint)
        }
    }
}
