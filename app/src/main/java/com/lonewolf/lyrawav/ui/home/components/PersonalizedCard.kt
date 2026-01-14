package com.lonewolf.lyrawav.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Radio
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lonewolf.lyrawav.ui.common.SectionTitle
import com.lonewolf.lyrawav.ui.theme.Poppins

@Composable
fun PersonalizedSection() {
    // Seção de exploração
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(text = "Explorar")

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { DiscoveryChip("Em Alta", Color(0xFF00BFA5), Icons.AutoMirrored.Filled.TrendingUp) }
            item { DiscoveryChip("Top 50", Color(0xFF6200EE), Icons.Default.Star) }
            item { DiscoveryChip("Lançamentos", Color(0xFFFF5252), Icons.Rounded.MusicNote) }
            item { DiscoveryChip("Mixes", Color(0xFFFFAB40), Icons.Rounded.LibraryMusic) }
            item { DiscoveryChip("Comunidade", Color(0xFF1E88E5), Icons.Default.Groups) }
            item { DiscoveryChip("Rádios", Color(0xFFFDD835), Icons.Rounded.Radio) }
        }
    }
}

@Composable
fun DiscoveryChip(title: String, color: Color, icon: ImageVector) {
    // Chip individual
    Row(
        modifier = Modifier
            .width(170.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.12f))
            .border(0.5.dp, color.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
            .clickable { /* Navegação */ }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícone do chip
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(color.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(10.dp))

        Text(
            text = title,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
