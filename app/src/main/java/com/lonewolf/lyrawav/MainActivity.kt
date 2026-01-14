package com.lonewolf.lyrawav

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lonewolf.lyrawav.ui.home.HomeScreen
import com.lonewolf.lyrawav.ui.theme.LiraWavTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita o design de borda a borda (Edge-to-Edge)
        enableEdgeToEdge()

        setContent {
            // Aplica o tema personalizado do LyraWav
            LiraWavTheme {
                // Chama a tela principal
                HomeScreen()
            }
        }
    }
}