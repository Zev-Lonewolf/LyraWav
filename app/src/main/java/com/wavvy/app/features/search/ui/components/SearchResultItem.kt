package com.wavvy.app.features.search.ui.components

// Compose layouts and foundations
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
// Material 3 components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.NorthWest
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
// UI styling and utilities
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
// Project resources
import com.wavvy.app.core.designsystem.theme.Poppins

// Menu action button
@Composable
private fun ItemMenuButton(onClick: () -> Unit = {}) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(36.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = "Mais opções",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(18.dp)
        )
    }
}

// Top match search result card
@Composable
fun TopMatchCard(
    item: SearchResultData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail avatar container
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(if (item.explicitType == SearchItemType.ARTIST || item.isArtist) CircleShape else RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (!item.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Title and subtitle labels container
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                modifier = Modifier.basicMarquee()
            )

            if (!item.subtitle.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.subtitle,
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee()
                )
            }
        }

        ItemMenuButton()
    }
}

// Song search result item
@Composable
fun SongResultItem(
    item: SearchResultData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SearchResultRow(
        title = item.title,
        subtitle = item.subtitle,
        imageUrl = item.imageUrl,
        isCircle = false,
        onClick = onClick,
        showMenu = true,
        modifier = modifier
    )
}

// Artist search result item
@Composable
fun ArtistResultItem(
    item: SearchResultData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SearchResultRow(
        title = item.title,
        subtitle = item.subtitle,
        imageUrl = item.imageUrl,
        isCircle = true,
        onClick = onClick,
        showMenu = true,
        modifier = modifier
    )
}

// Album search result item
@Composable
fun AlbumResultItem(
    item: SearchResultData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SearchResultRow(
        title = item.title,
        subtitle = item.subtitle,
        imageUrl = item.imageUrl,
        isCircle = false,
        onClick = onClick,
        showMenu = true,
        modifier = modifier
    )
}

// Video search result item
@Composable
fun VideoResultItem(
    item: SearchResultData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = MaterialTheme.colorScheme.surfaceVariant

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail container
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            if (!item.imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Title and subtitle labels container
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                modifier = Modifier.basicMarquee()
            )

            if (!item.subtitle.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.subtitle,
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee()
                )
            }
        }

        ItemMenuButton()
    }
}

// Playlist search result item
@Composable
fun PlaylistResultItem(
    item: SearchResultData,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SearchResultRow(
        title = item.title,
        subtitle = item.subtitle,
        imageUrl = item.imageUrl,
        isCircle = false,
        onClick = onClick,
        showMenu = true,
        modifier = modifier
    )
}

// Album grid display item
@Composable
fun AlbumGridItem(
    title: String,
    subtitle: String?,
    imageUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = MaterialTheme.colorScheme.surfaceVariant

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(6.dp)
    ) {
        // Album cover container
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(6.dp))
                .background(containerColor)
        ) {
            if (!imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            modifier = Modifier.basicMarquee()
        )

        if (!subtitle.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontFamily = Poppins,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                modifier = Modifier.basicMarquee()
            )
        }
    }
}

// General search result row component
@Composable
fun SearchResultRow(
    title: String,
    subtitle: String? = null,
    imageUrl: String? = null,
    isCircle: Boolean = false,
    showMenu: Boolean = false,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val containerColor = MaterialTheme.colorScheme.surfaceVariant

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Thumbnail container
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(if (isCircle) CircleShape else RoundedCornerShape(6.dp))
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            if (!imageUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Title and subtitle labels container
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                modifier = Modifier.basicMarquee()
            )

            if (!subtitle.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    modifier = Modifier.basicMarquee()
                )
            }
        }

        if (showMenu) {
            ItemMenuButton()
        }
    }
}

// Text autocomplete suggestion item row
@Composable
fun SearchTextSuggestionItem(
    queryText: String,
    onClick: () -> Unit,
    onInsertClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Rounded.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = queryText,
            fontFamily = Poppins,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .basicMarquee()
        )

        // Insert action button
        IconButton(
            onClick = { onInsertClick?.invoke() },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.NorthWest,
                contentDescription = "Inserir texto",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// Fallback search result item
@Composable
fun SearchResultItem(
    title: String,
    subtitle: String? = null,
    imageUrl: String? = null,
    isArtist: Boolean = false,
    onClick: () -> Unit = {}
) {
    SearchResultRow(
        title = title,
        subtitle = subtitle,
        imageUrl = imageUrl,
        isCircle = isArtist,
        showMenu = true,
        onClick = onClick
    )
}
