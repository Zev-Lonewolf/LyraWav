package com.lonewolf.lyrawav.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lonewolf.lyrawav.ui.common.SectionTitle
import com.lonewolf.lyrawav.ui.theme.Poppins

@Composable
fun FinalPilaresSection() {
    // Seções finais (IA, Podcasts e Lives)
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 110.dp)) {
        SectionTitle(text = "Lyra Live • IA")
        RadioIACard()

        Spacer(modifier = Modifier.height(24.dp))
        SectionTitle(text = "Podcasts para você")
        PodcastsRow()

        Spacer(modifier = Modifier.height(24.dp))
        SectionTitle(text = "Lives agora")
        LivesRow()
    }
}

@Composable
fun RadioIACard() {
    // Card de Rádio IA
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(115.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF1A237E), Color(0xFF4A148C), Color(0xFF880E4F))))
            .clickable { }
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.GraphicEq, null, modifier = Modifier.size(42.dp), tint = Color.White)

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Sua Rádio IA",
                    color = Color.White,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 1
                )
                Text(
                    text = "Programação personalizada agora...",
                    color = Color.White.copy(alpha = 0.7f),
                    fontFamily = Poppins,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(end = 40.dp)
                )
            }
        }

        // Badge
        Surface(
            color = Color.Red,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Text(
                text = "AO VIVO",
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PodcastsRow() {
    // Lista de podcasts
    val pods = List(5) { "Podcast ${it + 1}" }
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(pods) { pod ->
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Text(text = pod, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, fontFamily = Poppins)
            }
        }
    }
}

@Composable
fun LivesRow() {
    // Lista de lives
    val lives = List(5) { "Live ${it + 1}" }
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(lives) { live ->
            Column(Modifier.width(150.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(85.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
                Text(
                    text = live,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.sp,
                    fontFamily = Poppins,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun MoodSection() {
    // Seção de vibes
    val vibes = listOf("Energia", "Relax", "Foco", "Melancolia", "Romance", "Viagem")
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(text = "Sintonize sua Vibe")
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(vibes) { vibe ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(80.dp).clip(CircleShape).background(MaterialTheme.colorScheme.surfaceVariant))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = vibe, fontSize = 12.sp, fontFamily = Poppins, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}
