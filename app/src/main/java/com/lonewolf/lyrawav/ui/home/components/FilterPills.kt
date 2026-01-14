package com.lonewolf.lyrawav.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lonewolf.lyrawav.ui.theme.Poppins

@Composable
fun FilterPills() {
    val filters = listOf("Foco", "Treino", "No Carro", "Relaxar", "Festa", "Viagem")
    var selectedFilter by remember { mutableStateOf("") }

    // Lista de filtros
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filters) { filter ->
            val isSelected = selectedFilter == filter

            // Pill individual
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.surface
                    )
                    .clickable { selectedFilter = if (isSelected) "" else filter }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = filter,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp
                    ),
                    color = if (isSelected) MaterialTheme.colorScheme.surface
                    else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
