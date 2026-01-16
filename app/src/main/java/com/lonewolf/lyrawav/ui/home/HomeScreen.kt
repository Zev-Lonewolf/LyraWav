package com.lonewolf.lyrawav.ui.home

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lonewolf.lyrawav.ui.common.FloatingNavBar
import com.lonewolf.lyrawav.ui.common.MiniPlayerPill
import com.lonewolf.lyrawav.ui.home.components.*

@Composable
fun HomeScreen(
    userName: String? = null,
    initialSongTitle: String? = null,
    initialArtistName: String? = null
) {
    var isPlaying by rememberSaveable { mutableStateOf(false) }
    var currentPosition by rememberSaveable { mutableLongStateOf(0L) }
    val duration = 215000L

    // Player expandido
    var isMiniPlayerActive by rememberSaveable { mutableStateOf(initialSongTitle != null) }
    var currentSongTitle by rememberSaveable { mutableStateOf(initialSongTitle ?: "") }
    var currentArtistName by rememberSaveable { mutableStateOf(initialArtistName ?: "") }
    var isPlayerExpanded by rememberSaveable { mutableStateOf(false) }

    // Simulação de progresso
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (isPlaying) {
                kotlinx.coroutines.delay(1000L)
                if (currentPosition < duration) currentPosition += 1000L else isPlaying = false
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

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        // Lista de conteúdo
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item { HomeHeader(onNavigateToSettings = { }) }
            item { GreetingSection(userName = userName) }
            item { FilterPills() }
            item { FastMusicGrid(onItemClick = { onMusicClick(it, "LyraWav") }) }
            item { RecentSection(onItemClick = { onMusicClick(it, "Recent") }) }
            item { PersonalizedSection(onItemClick = { }) }
            item { ArtistSection(onItemClick = { }) }
            item { GenreSection(onItemClick = { }) }
            item { MoodSection(onItemClick = { mood -> onMusicClick(mood, "Mix") }) }
            item {
                FinalPilaresSection(onItemClick = { title ->
                    if (!title.contains("IA")) onMusicClick(title, "LyraWav")
                })
            }
            item { Spacer(modifier = Modifier.height(180.dp)) }
        }

        // Camada de Interface (Player e NavBar)
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Player com animação de entrada (slide up)
            AnimatedVisibility(
                visible = isMiniPlayerActive && currentSongTitle.isNotEmpty(),
                enter = slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = spring(dampingRatio = 0.75f, stiffness = 200f)
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = spring(stiffness = 500f)
                ) + fadeOut()
            ) {
                MiniPlayerPill(
                    isExpanded = isPlayerExpanded,
                    showPlayer = true,
                    songTitle = currentSongTitle,
                    artistName = currentArtistName,
                    isPlaying = isPlaying,
                    onPlayPauseClick = { isPlaying = !isPlaying },
                    onDismiss = { isMiniPlayerActive = false },
                    onPillClick = { isPlayerExpanded = !isPlayerExpanded },
                    modifier = Modifier
                        .then(
                            if (isPlayerExpanded) Modifier.fillMaxSize()
                            else Modifier
                                .navigationBarsPadding()
                                .padding(bottom = 92.dp)
                        )
                )
            }

            // NavBar Flutuante
            AnimatedVisibility(
                visible = !isPlayerExpanded,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Column(
                    modifier = Modifier.navigationBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FloatingNavBar()
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
