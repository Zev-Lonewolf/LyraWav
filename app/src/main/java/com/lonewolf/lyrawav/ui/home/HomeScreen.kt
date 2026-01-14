package com.lonewolf.lyrawav.ui.home

import HomeHeader
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lonewolf.lyrawav.ui.common.SectionTitle
import com.lonewolf.lyrawav.ui.home.components.ArtistSection
import com.lonewolf.lyrawav.ui.home.components.FastMusicGrid
import com.lonewolf.lyrawav.ui.home.components.FilterPills
import com.lonewolf.lyrawav.ui.home.components.FloatingNavBar
import com.lonewolf.lyrawav.ui.home.components.RecentSection

@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()

    // O Box é o segredo: ele empilha os componentes
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
        ) {
            HomeHeader()
            GreetingSection(userName = null)
            FilterPills()
            SectionTitle(text = "Escolhas rápidas")
            FastMusicGrid()
            RecentSection()
            ArtistSection()

            // Esse espaço no final é essencial para os artistas não
            // ficarem escondidos atrás da barra ao rolar tudo
            Spacer(modifier = Modifier.height(110.dp))
        }

        // A barra fica fora da Column, mas dentro do Box
        FloatingNavBar()
    }
}