package com.wavvy.app.features.player.ui.components

// Android system
import android.content.Intent
// Compose foundation and graphics
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
// Material 3 components and icons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.PlaylistAdd
import androidx.compose.material.icons.automirrored.rounded.PlaylistPlay
import androidx.compose.material.icons.automirrored.rounded.QueueMusic
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Cast
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Radio
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
// Compose runtime & UI
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
// Project imports
import com.wavvy.app.R
import com.wavvy.app.core.designsystem.theme.Poppins

import com.wavvy.app.core.designsystem.theme.accentCyan

// Bottom sheet with options for Queue items and Quick Picks
@Composable
fun QueueMoreOptionsSheet(
    title: String,
    artist: String,
    album: String?,
    videoId: String,
    imageUrl: String? = null,
    onDismiss: () -> Unit,
    onPlayNext: () -> Unit,
    onAddToEnd: () -> Unit,
    onRemoveFromQueue: () -> Unit,
    onReloadMetadata: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onShowToast: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()
    val onSurface = MaterialTheme.colorScheme.onSurface

    val iconAccentColor = if (isDark) MaterialTheme.accentCyan else onSurface
    val subTextColor = onSurface.copy(alpha = 0.7f)

    val cardBg = if (isDark) onSurface.copy(alpha = 0.08f) else onSurface.copy(alpha = 0.06f)

    val listState = rememberLazyListState()

    val nestedScrollConnection = remember(listState) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < 0 && !listState.canScrollForward) {
                    return Offset(0f, available.y)
                }
                return Offset.Zero
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                if (available.y < 0) {
                    return Offset(0f, available.y)
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                return Velocity.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                return Velocity.Zero
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Track header item
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(cardBg),
                    contentAlignment = Alignment.Center
                ) {
                    if (!imageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.MusicNote,
                            contentDescription = null,
                            tint = onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title.ifBlank { "Wavvy Music" },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        color = onSurface,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee()
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = artist.ifBlank { stringResource(R.string.default_artist_name) },
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = Poppins,
                            fontSize = 13.sp
                        ),
                        color = if (isDark) iconAccentColor else onSurface.copy(alpha = 0.75f),
                        maxLines = 1,
                        modifier = Modifier.basicMarquee()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Top action cards
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MoreOptionsTopCard(
                    title = stringResource(R.string.more_opt_comments),
                    icon = Icons.Rounded.ChatBubbleOutline,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        if (videoId.isNotBlank()) {
                            onShowToast(videoId)
                        }
                    }
                )

                MoreOptionsTopCard(
                    title = stringResource(R.string.more_opt_info),
                    icon = Icons.Rounded.Info,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    modifier = Modifier.weight(1f),
                    onClick = {}
                )

                MoreOptionsTopCard(
                    title = stringResource(R.string.more_opt_share),
                    icon = Icons.Rounded.Share,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val cleanId = when {
                            videoId.contains("v=") -> videoId.substringAfter("v=").substringBefore("&").take(11)
                            videoId.contains("/shorts/") -> videoId.substringAfter("/shorts/").substringBefore("?").take(11)
                            videoId.contains("youtu.be/") -> videoId.substringAfter("youtu.be/").substringBefore("?").take(11)
                            else -> videoId.trim().take(11)
                        }
                        val sendIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, "https://music.youtube.com/watch?v=$cleanId")
                        }
                        onDismiss()
                        context.startActivity(Intent.createChooser(sendIntent, "Share via"))
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Option list rows
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MoreOptionsRow(
                    title = stringResource(R.string.more_opt_download),
                    icon = Icons.Rounded.Download,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    onClick = {}
                )

                MoreOptionsRow(
                    title = stringResource(R.string.more_opt_add_to_playlist),
                    icon = Icons.AutoMirrored.Rounded.PlaylistAdd,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    onClick = {}
                )

                MoreOptionsRow(
                    title = stringResource(R.string.more_opt_reload),
                    subtitle = stringResource(R.string.more_opt_reload_metadata),
                    icon = Icons.Rounded.Refresh,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    onClick = {
                        onDismiss()
                        onReloadMetadata()
                    }
                )

                MoreOptionsRow(
                    title = stringResource(R.string.more_opt_start_radio),
                    icon = Icons.Rounded.Radio,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    onClick = {}
                )

                MoreOptionsRow(
                    title = stringResource(R.string.more_opt_play_next),
                    icon = Icons.AutoMirrored.Rounded.PlaylistPlay,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    onClick = {
                        onDismiss()
                        onPlayNext()
                    }
                )

                MoreOptionsRow(
                    title = stringResource(R.string.more_opt_add_to_end),
                    icon = Icons.AutoMirrored.Rounded.QueueMusic,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    onClick = {
                        onDismiss()
                        onAddToEnd()
                    }
                )

                MoreOptionsRow(
                    title = stringResource(R.string.more_opt_remove_from_queue),
                    icon = Icons.Rounded.Delete,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    onClick = {
                        onDismiss()
                        onRemoveFromQueue()
                    }
                )

                MoreOptionsRow(
                    title = stringResource(R.string.more_opt_view_album),
                    subtitle = if (!album.isNullOrBlank()) album else stringResource(R.string.more_opt_no_album),
                    icon = Icons.Rounded.Album,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    onClick = {}
                )

                MoreOptionsRow(
                    title = stringResource(R.string.more_opt_view_artist),
                    subtitle = artist.ifBlank { stringResource(R.string.default_artist_name) },
                    icon = Icons.Rounded.Person,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(18.dp))
        }

        // Other sources section
        item {
            Text(
                text = stringResource(R.string.more_opt_other_sources),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = Poppins,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = subTextColor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MoreOptionsSisterCard(
                    title = stringResource(R.string.more_opt_listen_together),
                    icon = Icons.Rounded.Group,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    modifier = Modifier.weight(1f),
                    onClick = {}
                )

                MoreOptionsSisterCard(
                    title = stringResource(R.string.more_opt_cast),
                    icon = Icons.Rounded.Cast,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    modifier = Modifier.weight(1f),
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        // Settings option row
        item {
            MoreOptionsRow(
                title = stringResource(R.string.more_opt_settings),
                icon = Icons.Rounded.Settings,
                iconColor = iconAccentColor,
                backgroundColor = cardBg,
                onClick = {
                    onDismiss()
                    onNavigateToSettings()
                }
            )
        }
    }
}
