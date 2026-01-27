package com.lonewolf.wavvy.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lonewolf.wavvy.R
import com.lonewolf.wavvy.ui.common.SectionTitle
import com.lonewolf.wavvy.ui.theme.GenreGradients
import com.lonewolf.wavvy.ui.theme.Poppins

data class Genre(val nameResId: Int, val gradient: Brush)

@Composable
fun GenreSection(onItemClick: (String) -> Unit) {
    val genres = listOf(
        Genre(R.string.genre_pop, GenreGradients.pop),
        Genre(R.string.genre_rock, GenreGradients.rock),
        Genre(R.string.genre_hiphop, GenreGradients.hiphop),
        Genre(R.string.genre_electronic, GenreGradients.electronic),
        Genre(R.string.genre_indie, GenreGradients.indie),
        Genre(R.string.genre_lofi, GenreGradients.lofi),
        Genre(R.string.genre_jazz, GenreGradients.jazz),
        Genre(R.string.genre_soul, GenreGradients.soul),
        Genre(R.string.genre_rnb, GenreGradients.rnb),
        Genre(R.string.genre_ambient, GenreGradients.ambient),
        Genre(R.string.genre_metal, GenreGradients.metal),
        Genre(R.string.genre_punk, GenreGradients.punk),
        Genre(R.string.genre_hardrock, GenreGradients.hardrock),
        Genre(R.string.genre_phonk, GenreGradients.phonk),
        Genre(R.string.genre_trap, GenreGradients.trap),

        // Mediterrâneo e Oriente Médio
        Genre(R.string.genre_flamenco, GenreGradients.flamenco),
        Genre(R.string.genre_arabic, GenreGradients.arabic),
        Genre(R.string.genre_greek, GenreGradients.greek),

        // Ásia
        Genre(R.string.genre_kpop, GenreGradients.kpop),
        Genre(R.string.genre_jpop, GenreGradients.jpop),
        Genre(R.string.genre_cpop, GenreGradients.cpop),
        Genre(R.string.genre_hindustani, GenreGradients.hindustani),

        // Brasil e América Latina
        Genre(R.string.genre_mpb, GenreGradients.mpb),
        Genre(R.string.genre_funk, GenreGradients.funk),
        Genre(R.string.genre_sertanejo, GenreGradients.sertanejo),
        Genre(R.string.genre_pagode, GenreGradients.pagode),
        Genre(R.string.genre_rap_nacional, GenreGradients.rapNacional),
        Genre(R.string.genre_reggaeton, GenreGradients.reggaeton),

        // África e Caribe
        Genre(R.string.genre_afrobeat, GenreGradients.afrobeat),
        Genre(R.string.genre_reggae, GenreGradients.reggae),

        // Estética e Retro
        Genre(R.string.genre_vaporwave, GenreGradients.vaporwave),
        Genre(R.string.genre_synthwave, GenreGradients.synthwave),
        Genre(R.string.genre_citypop, GenreGradients.citypop)
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(text = stringResource(R.string.section_title_genres))

        LazyHorizontalGrid(
            rows = GridCells.Fixed(3),
            modifier = Modifier.height(210.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(genres) { genre ->
                GenreCard(genre, onItemClick)
            }
        }
    }
}

@Composable
fun GenreCard(genre: Genre, onItemClick: (String) -> Unit) {
    val genreName = stringResource(genre.nameResId)

    Box(
        modifier = Modifier
            .width(160.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(genre.gradient)
            .clickable { onItemClick(genreName) }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = genreName,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
        )
    }
}
