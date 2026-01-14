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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lonewolf.lyrawav.R
import com.lonewolf.lyrawav.ui.common.SectionTitle
import com.lonewolf.lyrawav.ui.theme.Poppins

@Composable
fun PersonalizedSection(onItemClick: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(text = stringResource(R.string.section_title_explore))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Lista de chips mapeada para facilitar a manutenção
            val discoveryItems = listOf(
                Triple(R.string.chip_trending, Color(0xFF00BFA5), Icons.AutoMirrored.Filled.TrendingUp),
                Triple(R.string.chip_top_50, Color(0xFF6200EE), Icons.Default.Star),
                Triple(R.string.chip_releases, Color(0xFFFF5252), Icons.Rounded.MusicNote),
                Triple(R.string.chip_mixes, Color(0xFFFFAB40), Icons.Rounded.LibraryMusic),
                Triple(R.string.chip_community, Color(0xFF1E88E5), Icons.Default.Groups),
                Triple(R.string.chip_radios, Color(0xFFFDD835), Icons.Rounded.Radio)
            )

            items(discoveryItems.size) { index ->
                val (titleRes, color, icon) = discoveryItems[index]
                val title = stringResource(titleRes)
                DiscoveryChip(
                    title = title,
                    color = color,
                    icon = icon,
                    onClick = { onItemClick(title) }
                )
            }
        }
    }
}

@Composable
fun DiscoveryChip(
    title: String,
    color: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    // Chip clicável de exploração
    Row(
        modifier = Modifier
            .width(170.dp)
            .height(60.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.12f))
            .border(
                0.5.dp,
                color.copy(alpha = 0.25f),
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ícone com destaque visual
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    color.copy(alpha = 0.2f),
                    RoundedCornerShape(10.dp)
                ),
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
