package com.wavvy.app.features.player.ui.components

// Compose foundation and graphics
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Cast
import androidx.compose.material.icons.rounded.ChatBubbleOutline
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Equalizer
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Radio
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.SmartDisplay
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
// Compose runtime & UI
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
// Project imports
import com.wavvy.app.R
import com.wavvy.app.core.designsystem.theme.Poppins
import com.wavvy.app.core.designsystem.theme.accentCyan

// Player options sheet
@Composable
fun PlayerMoreOptionsSheet(
    title: String,
    artist: String,
    album: String?,
    videoId: String,
    modifier: Modifier = Modifier,
    imageUrl: String? = null,
    onDismiss: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onShowToast: (String) -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()
    val onSurface = MaterialTheme.colorScheme.onSurface

    val iconAccentColor = if (isDark) MaterialTheme.accentCyan else onSurface
    val subTextColor = onSurface.copy(alpha = 0.7f)

    val cardBg = if (isDark) onSurface.copy(alpha = 0.08f) else onSurface.copy(alpha = 0.06f)

    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
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

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title.ifBlank { "Wavvy Music" },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        ),
                        color = onSurface,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee()
                    )

                    Spacer(modifier = Modifier.height(3.dp))

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
                    title = stringResource(R.string.more_opt_watch_clip),
                    icon = Icons.Rounded.SmartDisplay,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    modifier = Modifier.weight(1f),
                    onClick = {}
                )

                MoreOptionsTopCard(
                    title = stringResource(R.string.more_opt_snippet),
                    icon = Icons.Rounded.GraphicEq,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    modifier = Modifier.weight(1f),
                    onClick = {}
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
                    title = stringResource(R.string.more_opt_info),
                    icon = Icons.Rounded.Info,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    onClick = {}
                )

                MoreOptionsRow(
                    title = stringResource(R.string.more_opt_add_to_library),
                    icon = Icons.AutoMirrored.Rounded.PlaylistAdd,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    onClick = {}
                )

                MoreOptionsRow(
                    title = stringResource(R.string.more_opt_download),
                    icon = Icons.Rounded.Download,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    onClick = {}
                )

                MoreOptionsRow(
                    title = stringResource(R.string.more_opt_equalizer),
                    icon = Icons.Rounded.Equalizer,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    onClick = {}
                )

                MoreOptionsRow(
                    title = stringResource(R.string.more_opt_timer),
                    icon = Icons.Rounded.Timer,
                    iconColor = iconAccentColor,
                    backgroundColor = cardBg,
                    onClick = {}
                )

                MoreOptionsRow(
                    title = stringResource(R.string.more_opt_start_radio),
                    icon = Icons.Rounded.Radio,
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

                MoreOptionsRow(
                    title = stringResource(R.string.more_opt_view_album),
                    subtitle = if (!album.isNullOrBlank()) album else stringResource(R.string.more_opt_no_album),
                    icon = Icons.Rounded.Album,
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

// Top action card
@Composable
fun MoreOptionsTopCard(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(84.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = Poppins,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                modifier = Modifier.basicMarquee()
            )
        }
    }
}

// Option row
@Composable
fun MoreOptionsRow(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee()
                )
                if (!subtitle.isNullOrBlank()) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = Poppins,
                            fontSize = 12.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        maxLines = 1,
                        modifier = Modifier.basicMarquee()
                    )
                }
            }
        }
    }
}

// Sister card
@Composable
fun MoreOptionsSisterCard(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = Poppins,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                modifier = Modifier.basicMarquee()
            )
        }
    }
}
