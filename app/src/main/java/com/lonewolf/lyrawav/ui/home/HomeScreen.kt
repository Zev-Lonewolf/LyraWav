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
    // Estado do player - MANTIDO ENTRE MINIMIZAR/EXPANDIR
    var isPlaying by rememberSaveable { mutableStateOf(false) }
    var currentPosition by rememberSaveable { mutableLongStateOf(0L) }
    var duration by rememberSaveable { mutableLongStateOf(0L) } // Será preenchido pelo player real

    // Estado da UI
    var isMiniPlayerActive by rememberSaveable { mutableStateOf(initialSongTitle != null) }
    var currentSongTitle by rememberSaveable { mutableStateOf(initialSongTitle ?: "") }
    var currentArtistName by rememberSaveable { mutableStateOf(initialArtistName ?: "") }
    var isPlayerExpanded by rememberSaveable { mutableStateOf(false) }

    // Função para mover o progresso livremente
    val onSeek: (Float) -> Unit = { percentage ->
        // Se não há duração definida, apenas atualiza a posição proporcionalmente
        // Quando conectar ao player real, isso funcionará corretamente
        currentPosition = if (duration > 0) {
            (duration * percentage).toLong()
        } else {
            // Permite mover mesmo sem duração (útil para teste)
            (100000L * percentage).toLong() // Valor temporário só para visualização
        }
    }

    val onMusicClick = { title: String, artist: String ->
        currentSongTitle = title
        currentArtistName = artist
        isMiniPlayerActive = true
        currentPosition = 0L // Reseta apenas ao clicar em nova música
        isPlaying = true
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
                    currentPosition = currentPosition,
                    duration = duration,
                    onPlayPauseClick = { isPlaying = !isPlaying },
                    onDismiss = {
                        isMiniPlayerActive = false
                        isPlaying = false
                        currentPosition = 0L
                    },
                    onPillClick = {
                        isPlayerExpanded = !isPlayerExpanded
                        // NÃO reseta o progresso, apenas alterna o estado
                    },
                    onNext = {
                        // Próxima música
                        currentPosition = 0L
                    },
                    onPrevious = {
                        // Música anterior
                        currentPosition = 0L
                    },
                    onSeek = onSeek,
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
