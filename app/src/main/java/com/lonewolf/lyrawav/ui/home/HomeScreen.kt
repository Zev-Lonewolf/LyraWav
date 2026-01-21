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
import com.lonewolf.lyrawav.ui.common.PlayerContainer
import com.lonewolf.lyrawav.ui.home.components.*

@Composable
fun HomeScreen(
    userName: String? = null
) {
    var isMiniPlayerActive by rememberSaveable { mutableStateOf(false) }
    var isPlayerExpanded by rememberSaveable { mutableStateOf(false) }
    var currentSongTitle by rememberSaveable { mutableStateOf("") }
    var currentArtistName by rememberSaveable { mutableStateOf("") }

    val onMusicClick = { title: String, artist: String ->
        currentSongTitle = title
        currentArtistName = artist
        isMiniPlayerActive = true
        isPlayerExpanded = false
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

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
            item { FinalPilaresSection(onItemClick = { title -> if (!title.contains("IA")) onMusicClick(title, "LyraWav") }) }
            item { Spacer(modifier = Modifier.height(180.dp)) }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            AnimatedVisibility(
                visible = isMiniPlayerActive && currentSongTitle.isNotEmpty(),
                enter = slideInVertically(initialOffsetY = { it }, animationSpec = spring(dampingRatio = 0.75f, stiffness = 200f)) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }, animationSpec = spring(stiffness = 500f)) + fadeOut()
            ) {
                PlayerContainer(
                    isExpanded = isPlayerExpanded,
                    songTitle = currentSongTitle,
                    artistName = currentArtistName,
                    onPillClick = { isPlayerExpanded = !isPlayerExpanded },
                    onDismiss = {
                        isMiniPlayerActive = false
                        isPlayerExpanded = false
                    },
                    onNext = { /* Próxima música */ },
                    onPrevious = { /* Música anterior */ },
                    modifier = if (isPlayerExpanded) {
                        Modifier.fillMaxSize()
                    } else {
                        Modifier
                            .padding(bottom = 72.dp)
                            .navigationBarsPadding()
                    }
                )
            }

            AnimatedVisibility(
                visible = !isPlayerExpanded,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Column(modifier = Modifier.navigationBarsPadding(), horizontalAlignment = Alignment.CenterHorizontally) {
                    FloatingNavBar()
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}
