package com.lonewolf.wavvy.ui.home.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lonewolf.wavvy.R
import com.lonewolf.wavvy.ui.common.SectionTitle
import com.lonewolf.wavvy.ui.theme.Poppins
import com.lonewolf.wavvy.ui.theme.gradientColors

@Composable
fun FinalPilaresSection(onItemClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 110.dp)
    ) {
        SectionTitle(text = stringResource(R.string.section_title_lyra_ia))
        RadioIACard(onItemClick)

        Spacer(modifier = Modifier.height(24.dp))
        SectionTitle(text = stringResource(R.string.section_title_podcasts))
        PodcastsRow(onItemClick)

        Spacer(modifier = Modifier.height(24.dp))
        SectionTitle(text = stringResource(R.string.section_title_lives))
        LivesRow(onItemClick)
    }
}

@Composable
fun RadioIACard(onItemClick: (String) -> Unit) {
    val title = stringResource(R.string.ia_radio_title)
    val subtitle = stringResource(R.string.ia_radio_subtitle)
    val liveBadge = stringResource(R.string.badge_live_now)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(115.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(MaterialTheme.gradientColors))
            .clickable { onItemClick(title) }
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.GraphicEq,
                contentDescription = null,
                modifier = Modifier.size(42.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 1
                )
                Text(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    fontFamily = Poppins,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(end = 40.dp)
                )
            }
        }

        Surface(
            color = MaterialTheme.colorScheme.error,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Text(
                text = liveBadge,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                color = MaterialTheme.colorScheme.onError,
                fontSize = 9.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun PodcastsRow(onItemClick: (String) -> Unit) {
    val podcastCount = 5
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(podcastCount) { index ->
            val podName = stringResource(R.string.placeholder_podcast, index + 1)

            Column(
                modifier = Modifier
                    .width(130.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onItemClick(podName) }
            ) {
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        modifier = Modifier.size(28.dp)
                    )
                }

                Text(
                    text = podName,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 12.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 8.dp, start = 4.dp)
                )
            }
        }
    }
}

@Composable
fun LivesRow(onItemClick: (String) -> Unit) {
    val liveCount = 5
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(liveCount) { index ->
            val liveName = stringResource(R.string.placeholder_live, index + 1)

            Column(
                modifier = Modifier
                    .width(150.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onItemClick(liveName) }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(85.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        modifier = Modifier.size(32.dp)
                    )
                }
                Text(
                    text = liveName,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 12.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun MoodSection(onItemClick: (String) -> Unit) {
    val vibeResIds = listOf(
        R.string.vibe_energy, R.string.vibe_relax, R.string.vibe_focus,
        R.string.vibe_melancholy, R.string.vibe_romance, R.string.vibe_travel
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(text = stringResource(R.string.section_title_moods))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(vibeResIds) { resId ->
                val moodName = stringResource(resId)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onItemClick(moodName) }
                        .padding(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = moodName,
                        fontSize = 12.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
