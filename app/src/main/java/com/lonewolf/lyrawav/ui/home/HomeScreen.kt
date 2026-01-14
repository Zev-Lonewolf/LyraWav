package com.lonewolf.lyrawav.ui.home

import HomeHeader
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lonewolf.lyrawav.ui.home.components.*

@Composable
fun HomeScreen(userName: String? = null) {
    // Tela principal
    Box(modifier = Modifier.fillMaxSize()) {
        // Conteúdo rolável
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            item { HomeHeader() }
            item { GreetingSection(userName = userName) }
            item { FilterPills() }
            item { FastMusicGrid() }
            item { RecentSection() }
            item { PersonalizedSection() }
            item { ArtistSection() }
            item { GenreSection() }
            item { MoodSection() }
            item { FinalPilaresSection() }
        }

        // Navegação fixa
        FloatingNavBar()
    }
}
