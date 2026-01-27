package com.lonewolf.wavvy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lonewolf.wavvy.ui.home.HomeScreen
import com.lonewolf.wavvy.ui.theme.WavvyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Habilita o design de borda a borda (Edge-to-Edge)
        enableEdgeToEdge()

        setContent {
            // Aplica o tema personalizado do LyraWav
            WavvyTheme {
                // Chama a tela principal + Player
                HomeScreen()
            }
        }
    }
}