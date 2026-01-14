package com.lonewolf.lyrawav.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lonewolf.lyrawav.ui.common.FloatingNavBar
import com.lonewolf.lyrawav.ui.common.MiniPlayerPill
import com.lonewolf.lyrawav.ui.home.components.*

@Composable
fun HomeScreen(userName: String? = null) {
    var isPlayerVisible by remember { mutableStateOf(false) }
    var currentSongTitle by remember { mutableStateOf("") }
    var currentArtistName by remember { mutableStateOf("Autor Desconhecido") }

    val onMusicClick = { title: String, artist: String ->
        currentSongTitle = title
        currentArtistName = artist
        isPlayerVisible = true
    }

    val onNavigateClick = { destination: String ->
        println("Navegando para: $destination")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item { HomeHeader(onNavigateToSettings = { }) }
            item { GreetingSection(userName = userName) }
            item { FilterPills() }

            item {
                FastMusicGrid(
                    onItemClick = { onMusicClick(it, "Canal LyraWav") }
                )
            }

            item {
                RecentSection(
                    onItemClick = { onMusicClick(it, "Artista") }
                )
            }

            item {
                PersonalizedSection(
                    onItemClick = { onNavigateClick(it) }
                )
            }

            item {
                ArtistSection(
                    onItemClick = { onNavigateClick(it) }
                )
            }

            item {
                GenreSection(
                    onItemClick = { onNavigateClick(it) }
                )
            }

            // AJUSTADO: Agora dispara o Player (Sintoniza a Vibe)
            item {
                MoodSection(
                    onItemClick = { onMusicClick("Mix: $it", "Sintonizado na sua Vibe") }
                )
            }

            item {
                FinalPilaresSection(
                    onItemClick = { title ->
                        if (title.contains("Lyra") || title.contains("IA")) {
                            onNavigateClick(title)
                        } else {
                            onMusicClick(title, "LyraWav Media")
                        }
                    }
                )
            }

            item { Spacer(modifier = Modifier.height(180.dp)) }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MiniPlayerPill(
                showPlayer = isPlayerVisible,
                songTitle = currentSongTitle,
                artistName = currentArtistName,
                onDismiss = { isPlayerVisible = false },
                onPillClick = { },
                onNext = { },
                onPrevious = { }
            )

            if (isPlayerVisible) {
                Spacer(modifier = Modifier.height(12.dp))
            }

            FloatingNavBar()
        }
    }
}
