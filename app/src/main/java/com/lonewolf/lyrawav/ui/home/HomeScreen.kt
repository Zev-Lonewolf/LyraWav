package com.lonewolf.lyrawav.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lonewolf.lyrawav.ui.common.FloatingNavBar
import com.lonewolf.lyrawav.ui.common.MiniPlayerPill
import com.lonewolf.lyrawav.ui.home.components.*
import com.lonewolf.lyrawav.ui.player.PlayerScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(userName: String? = null) {

    // Estado global do player
    var isPlaying by rememberSaveable { mutableStateOf(false) }
    var currentPosition by rememberSaveable { mutableLongStateOf(0L) }
    val duration = 215000L

    // Estado da música atual
    var isMiniPlayerActive by rememberSaveable { mutableStateOf(false) }
    var currentSongTitle by rememberSaveable { mutableStateOf("") }
    var currentArtistName by rememberSaveable { mutableStateOf("Autor Desconhecido") }

    // Player expandido
    var isPlayerExpanded by rememberSaveable { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    // Simulação de progresso
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (isPlaying) {
                kotlinx.coroutines.delay(1000L)
                if (currentPosition < duration) {
                    currentPosition += 1000L
                } else {
                    isPlaying = false
                }
            }
        }
    }

    val onMusicClick = { title: String, artist: String ->
        currentSongTitle = title
        currentArtistName = artist
        isMiniPlayerActive = true
        isPlaying = true
        currentPosition = 0L
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
            item { FastMusicGrid(onItemClick = { onMusicClick(it, "Canal LyraWav") }) }
            item { RecentSection(onItemClick = { onMusicClick(it, "Artista") }) }
            item { PersonalizedSection(onItemClick = { onNavigateClick(it) }) }
            item { ArtistSection(onItemClick = { onNavigateClick(it) }) }
            item { GenreSection(onItemClick = { onNavigateClick(it) }) }
            item { MoodSection(onItemClick = { onMusicClick("Mix: $it", "Sintonizado na sua Vibe") }) }
            item {
                FinalPilaresSection(onItemClick = { title ->
                    if (title.contains("Lyra") || title.contains("IA"))
                        onNavigateClick(title)
                    else
                        onMusicClick(title, "LyraWav Media")
                })
            }
            item { Spacer(modifier = Modifier.height(180.dp)) }
        }

        // MiniPlayer + NavBar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (isMiniPlayerActive && currentSongTitle.isNotEmpty()) {
                MiniPlayerPill(
                    showPlayer = true,
                    songTitle = currentSongTitle,
                    artistName = currentArtistName,
                    isPlaying = isPlaying,
                    onPlayPauseClick = { isPlaying = !isPlaying },
                    onDismiss = { isMiniPlayerActive = false },
                    onPillClick = { isPlayerExpanded = true },
                    onNext = { },
                    onPrevious = { }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            FloatingNavBar()
        }

        // Player completo
        if (isPlayerExpanded) {
            ModalBottomSheet(
                onDismissRequest = { isPlayerExpanded = false },
                sheetState = sheetState,
                dragHandle = null,
                containerColor = MaterialTheme.colorScheme.background,
                scrimColor = Color.Black.copy(alpha = 0.7f),
                contentWindowInsets = { WindowInsets(0) }
            ) {
                PlayerScreen(
                    title = currentSongTitle,
                    artist = currentArtistName,
                    isPlaying = isPlaying,
                    currentPosition = currentPosition,
                    duration = duration,
                    onPlayPauseToggle = { isPlaying = !isPlaying },
                    onSeek = { currentPosition = it },
                    onMinimize = { isPlayerExpanded = false }
                )
            }
        }
    }
}
