package com.lonewolf.lyrawav.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.lonewolf.lyrawav.ui.theme.Poppins

@Composable
fun SectionTitle(text: String) {
    // Título das seções (Escolhas rápidas, Recentemente, etc)
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge.copy(
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        ),
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}