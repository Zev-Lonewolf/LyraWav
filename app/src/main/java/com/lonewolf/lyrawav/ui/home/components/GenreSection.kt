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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lonewolf.lyrawav.R
import com.lonewolf.lyrawav.ui.common.SectionTitle
import com.lonewolf.lyrawav.ui.theme.Poppins

data class Genre(val nameResId: Int, val gradient: Brush)

@Composable
fun GenreSection(onItemClick: (String) -> Unit) {
    // Lista fixa de gêneros expandida com sons globais e regionais
    val genres = listOf(
        Genre(R.string.genre_pop, Brush.linearGradient(listOf(Color(0xFF8E24AA), Color(0xFFBA68C8)))),
        Genre(R.string.genre_rock, Brush.linearGradient(listOf(Color(0xFF263238), Color(0xFF455A64)))),
        Genre(R.string.genre_hiphop, Brush.linearGradient(listOf(Color(0xFFBF360C), Color(0xFFFF7043)))),
        Genre(R.string.genre_electronic, Brush.linearGradient(listOf(Color(0xFF1B5E20), Color(0xFF66BB6A)))),
        Genre(R.string.genre_indie, Brush.linearGradient(listOf(Color(0xFF283593), Color(0xFF5C6BC0)))),
        Genre(R.string.genre_lofi, Brush.linearGradient(listOf(Color(0xFF1A237E), Color(0xFF3F51B5)))),
        Genre(R.string.genre_jazz, Brush.linearGradient(listOf(Color(0xFF3E2723), Color(0xFF795548)))),
        Genre(R.string.genre_soul, Brush.linearGradient(listOf(Color(0xFF4E342E), Color(0xFFA1887F)))),
        Genre(R.string.genre_rnb, Brush.linearGradient(listOf(Color(0xFF311B92), Color(0xFF9575CD)))),
        Genre(R.string.genre_ambient, Brush.linearGradient(listOf(Color(0xFF004D40), Color(0xFF4DB6AC)))),
        Genre(R.string.genre_metal, Brush.linearGradient(listOf(Color(0xFF000000), Color(0xFF424242)))),
        Genre(R.string.genre_punk, Brush.linearGradient(listOf(Color(0xFFB71C1C), Color(0xFFD32F2F)))),
        Genre(R.string.genre_hardrock, Brush.linearGradient(listOf(Color(0xFF212121), Color(0xFF616161)))),
        Genre(R.string.genre_phonk, Brush.linearGradient(listOf(Color(0xFF004D40), Color(0xFF009688)))),
        Genre(R.string.genre_trap, Brush.linearGradient(listOf(Color(0xFF263238), Color(0xFF546E7A)))),

        // Mediterrâneo e Oriente Médio
        Genre(R.string.genre_flamenco, Brush.linearGradient(listOf(Color(0xFFD32F2F), Color(0xFFE64A19)))),
        Genre(R.string.genre_arabic, Brush.linearGradient(listOf(Color(0xFF5D4037), Color(0xFF8D6E63)))),
        Genre(R.string.genre_greek, Brush.linearGradient(listOf(Color(0xFF0277BD), Color(0xFF4FC3F7)))),

        // Ásia
        Genre(R.string.genre_kpop, Brush.linearGradient(listOf(Color(0xFFD81B60), Color(0xFFF48FB1)))),
        Genre(R.string.genre_jpop, Brush.linearGradient(listOf(Color(0xFFAD1457), Color(0xFFF06292)))),
        Genre(R.string.genre_cpop, Brush.linearGradient(listOf(Color(0xFFB71C1C), Color(0xFFFFD600)))),
        Genre(R.string.genre_hindustani, Brush.linearGradient(listOf(Color(0xFFE65100), Color(0xFFFFB74D)))),

        // Brasil e América Latina
        Genre(R.string.genre_mpb, Brush.linearGradient(listOf(Color(0xFF1B5E20), Color(0xFF81C784)))),
        Genre(R.string.genre_funk, Brush.linearGradient(listOf(Color(0xFF4A148C), Color(0xFFAB47BC)))),
        Genre(R.string.genre_sertanejo, Brush.linearGradient(listOf(Color(0xFF3E2723), Color(0xFFD7CCC8)))),
        Genre(R.string.genre_pagode, Brush.linearGradient(listOf(Color(0xFFBF360C), Color(0xFFFFAB91)))),
        Genre(R.string.genre_rap_nacional, Brush.linearGradient(listOf(Color(0xFF263238), Color(0xFF90A4AE)))),
        Genre(R.string.genre_reggaeton, Brush.linearGradient(listOf(Color(0xFFC2185B), Color(0xFFF06292)))),

        // África e Caribe
        Genre(R.string.genre_afrobeat, Brush.linearGradient(listOf(Color(0xFF1B5E20), Color(0xFFFBC02D)))),
        Genre(R.string.genre_reggae, Brush.linearGradient(listOf(Color(0xFF388E3C), Color(0xFFFBC02D)))),

        // Estética e Retro
        Genre(R.string.genre_vaporwave, Brush.linearGradient(listOf(Color(0xFF4A148C), Color(0xFFCE93D8)))),
        Genre(R.string.genre_synthwave, Brush.linearGradient(listOf(Color(0xFF0D47A1), Color(0xFF64B5F6)))),
        Genre(R.string.genre_citypop, Brush.linearGradient(listOf(Color(0xFF006064), Color(0xFF4DD0E1))))
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        // Título da seção
        SectionTitle(text = stringResource(R.string.section_title_genres))

        // Grid horizontal com rolagem lateral
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
    // Card individual de gênero
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(genre.gradient)
            .clickable { /* navegação futura */ }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = stringResource(genre.nameResId),
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.9f)
        )
    }
}
