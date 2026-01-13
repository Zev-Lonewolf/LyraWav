package com.lonewolf.lyrawav.ui.home

import HomeHeader
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lonewolf.lyrawav.ui.common.SectionTitle
import com.lonewolf.lyrawav.ui.home.components.FastMusicGrid

@Composable
fun HomeScreen() {
    val welcomeText = getWelcomeMessage(null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        HomeHeader()
        GreetingSection(userName = null)
        SectionTitle(text = "Escolhas rápidas")
        FastMusicGrid()
    }
}
