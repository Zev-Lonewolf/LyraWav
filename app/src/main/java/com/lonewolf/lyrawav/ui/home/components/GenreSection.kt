package com.lonewolf.lyrawav.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lonewolf.lyrawav.ui.common.SectionTitle
import com.lonewolf.lyrawav.ui.theme.Poppins

data class Genre(val name: String, val gradient: Brush)

@Composable
fun GenreSection() {
    val genres = listOf(
        Genre("Pop", Brush.linearGradient(listOf(Color(0xFF8E24AA), Color(0xFFBA68C8)))),
        Genre("Rock", Brush.linearGradient(listOf(Color(0xFF263238), Color(0xFF455A64)))),
        Genre("Hip Hop", Brush.linearGradient(listOf(Color(0xFFBF360C), Color(0xFFFF7043)))),
        Genre("Eletrônica", Brush.linearGradient(listOf(Color(0xFF1B5E20), Color(0xFF66BB6A)))),
        Genre("Indie", Brush.linearGradient(listOf(Color(0xFF283593), Color(0xFF5C6BC0)))),
        Genre("Lofi", Brush.linearGradient(listOf(Color(0xFF1A237E), Color(0xFF3F51B5)))),
        Genre("Jazz", Brush.linearGradient(listOf(Color(0xFF3E2723), Color(0xFF795548)))),
        Genre("Soul", Brush.linearGradient(listOf(Color(0xFF4E342E), Color(0xFFA1887F)))),
        Genre("R&B", Brush.linearGradient(listOf(Color(0xFF311B92), Color(0xFF9575CD)))),
        Genre("Ambient", Brush.linearGradient(listOf(Color(0xFF004D40), Color(0xFF4DB6AC)))),
        Genre("Metal", Brush.linearGradient(listOf(Color(0xFF000000), Color(0xFF424242)))),
        Genre("Punk", Brush.linearGradient(listOf(Color(0xFFB71C1C), Color(0xFFD32F2F)))),
        Genre("Hard Rock", Brush.linearGradient(listOf(Color(0xFF212121), Color(0xFF616161)))),
        Genre("Phonk", Brush.linearGradient(listOf(Color(0xFF004D40), Color(0xFF009688)))),
        Genre("Trap", Brush.linearGradient(listOf(Color(0xFF263238), Color(0xFF546E7A)))),
        Genre("MPB", Brush.linearGradient(listOf(Color(0xFF1B5E20), Color(0xFF81C784)))),
        Genre("Funk", Brush.linearGradient(listOf(Color(0xFF4A148C), Color(0xFFAB47BC)))),
        Genre("Sertanejo", Brush.linearGradient(listOf(Color(0xFF3E2723), Color(0xFFD7CCC8)))),
        Genre("Pagode", Brush.linearGradient(listOf(Color(0xFFBF360C), Color(0xFFFFAB91)))),
        Genre("Rap Nacional", Brush.linearGradient(listOf(Color(0xFF263238), Color(0xFF90A4AE)))),
        Genre("K-Pop", Brush.linearGradient(listOf(Color(0xFFD81B60), Color(0xFFF48FB1)))),
        Genre("J-Pop", Brush.linearGradient(listOf(Color(0xFFAD1457), Color(0xFFF06292)))),
        Genre("Vaporwave", Brush.linearGradient(listOf(Color(0xFF4A148C), Color(0xFFCE93D8)))),
        Genre("Synthwave", Brush.linearGradient(listOf(Color(0xFF0D47A1), Color(0xFF64B5F6)))),
        Genre("City Pop", Brush.linearGradient(listOf(Color(0xFF006064), Color(0xFF4DD0E1))))
    )

    // Lista de gêneros
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(text = "Vibes & Gêneros")

        LazyHorizontalGrid(
            rows = GridCells.Fixed(3),
            modifier = Modifier.height(210.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(genres) { genre ->
                GenreCard(genre)
            }
        }
    }
}

@Composable
fun GenreCard(genre: Genre) {
    // Card individual
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(genre.gradient)
            .clickable { }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = genre.name,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.9f)
        )
    }
}
