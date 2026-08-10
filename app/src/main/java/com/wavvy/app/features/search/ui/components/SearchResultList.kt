package com.wavvy.app.features.search.ui.components

// Compose layouts and foundations
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
// Material 3 components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
// UI styling and utilities
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Project resources
import com.wavvy.app.R
import com.wavvy.app.core.designsystem.components.SearchCategory
import com.wavvy.app.core.designsystem.theme.Poppins

// Search item types
enum class SearchItemType {
    TOP_MATCH,
    SONG,
    ARTIST,
    ALBUM,
    VIDEO,
    PLAYLIST,
    EPISODE,
    PODCAST,
    PROFILE,
    UNKNOWN
}

// Search result item state
data class SearchResultData(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val imageUrl: String? = null,
    val isArtist: Boolean = false,
    val videoId: String? = null,
    val browseId: String? = null,
    val category: SearchCategory = SearchCategory.SONGS,
    val explicitType: SearchItemType = SearchItemType.UNKNOWN,
    val isTopMatch: Boolean = false
)

// Main search result list layout
@Composable
fun SearchResultList(
    modifier: Modifier = Modifier,
    category: SearchCategory,
    results: List<SearchResultData>,
    isLoadingMore: Boolean = false,
    loadingCategory: SearchCategory? = null,
    reachedEndOfCategory: Set<SearchCategory> = emptySet(),
    onLoadMore: () -> Unit = {},
    onFetchMoreCategory: ((SearchCategory) -> Unit)? = null,
    onItemClick: (SearchResultData) -> Unit,
    onSeeAll: ((SearchCategory) -> Unit)? = null,
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (results.isEmpty()) {
            SearchEmptyState()
        } else {
            when (category) {
                SearchCategory.ALL -> {
                    AllResultsSummary(
                        results = results,
                        loadingCategory = loadingCategory,
                        reachedEndOfCategory = reachedEndOfCategory,
                        onFetchMoreCategory = onFetchMoreCategory,
                        onItemClick = onItemClick
                    )
                }

                SearchCategory.ALBUMS -> {
                    val gridState = androidx.compose.foundation.lazy.grid.rememberLazyGridState()
                    val shouldLoadMore by remember {
                        derivedStateOf {
                            val totalItems = gridState.layoutInfo.totalItemsCount
                            val lastVisible = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                            totalItems > 0 && lastVisible >= totalItems - 4
                        }
                    }
                    LaunchedEffect(shouldLoadMore) {
                        if (shouldLoadMore) onLoadMore()
                    }

                    LazyVerticalGrid(
                        state = gridState,
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 12.dp, top = 12.dp, end = 12.dp, bottom = 220.dp)
                    ) {
                        items(items = results, key = { it.id }) { album ->
                            AlbumGridItem(
                                title = album.title,
                                subtitle = album.subtitle,
                                imageUrl = album.imageUrl,
                                onClick = { onItemClick(album) }
                            )
                        }

                        if (isLoadingMore) {
                            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                                LoadingMoreIndicator()
                            }
                        } else if (SearchCategory.ALBUMS in reachedEndOfCategory) {
                            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                                SectionEndIndicator()
                            }
                        }
                    }
                }

                else -> {
                    key(category) {
                        val listState = androidx.compose.foundation.lazy.rememberLazyListState()
                        val shouldLoadMore by remember {
                            derivedStateOf {
                                val totalItems = listState.layoutInfo.totalItemsCount
                                val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
                                totalItems > 0 && lastVisible >= totalItems - 4
                            }
                        }
                        LaunchedEffect(shouldLoadMore) {
                            if (shouldLoadMore) onLoadMore()
                        }

                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 220.dp)
                        ) {
                            items(items = results, key = { it.id }) { item ->
                                SearchItemByType(
                                    item = item,
                                    category = category,
                                    onClick = { onItemClick(item) }
                                )
                            }

                            if (isLoadingMore) {
                                item { LoadingMoreIndicator() }
                            } else if (category in reachedEndOfCategory) {
                                item { SectionEndIndicator() }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Search result item type router
@Composable
private fun SearchItemByType(
    item: SearchResultData,
    category: SearchCategory,
    onClick: () -> Unit
) {
    val effectiveType = when {
        category == SearchCategory.VIDEOS -> SearchItemType.VIDEO
        category == SearchCategory.ARTISTS || item.isArtist -> SearchItemType.ARTIST
        category == SearchCategory.ALBUMS -> SearchItemType.ALBUM
        category == SearchCategory.COMMUNITY_PLAYLISTS || category == SearchCategory.FEATURED_PLAYLISTS -> SearchItemType.PLAYLIST
        category == SearchCategory.EPISODES -> SearchItemType.EPISODE
        category == SearchCategory.PODCASTS -> SearchItemType.PODCAST
        category == SearchCategory.PROFILES -> SearchItemType.PROFILE
        item.explicitType != SearchItemType.UNKNOWN -> item.explicitType
        else -> SearchItemType.SONG
    }

    when (effectiveType) {
        SearchItemType.ARTIST, SearchItemType.PROFILE -> ArtistResultItem(item = item, onClick = onClick)
        SearchItemType.VIDEO -> VideoResultItem(item = item, onClick = onClick)
        SearchItemType.PLAYLIST, SearchItemType.EPISODE, SearchItemType.PODCAST -> PlaylistResultItem(item = item, onClick = onClick)
        else -> SongResultItem(item = item, onClick = onClick)
    }
}

// Combined categories search overview
@Composable
private fun AllResultsSummary(
    results: List<SearchResultData>,
    loadingCategory: SearchCategory?,
    reachedEndOfCategory: Set<SearchCategory>,
    onFetchMoreCategory: ((SearchCategory) -> Unit)?,
    onItemClick: (SearchResultData) -> Unit
) {
    val topMatch = results.firstOrNull { it.isTopMatch }
    val regularItems = results.filter { !it.isTopMatch }

    val allSongs     = regularItems.filter { it.explicitType == SearchItemType.SONG }
    val allArtists   = regularItems.filter { it.explicitType == SearchItemType.ARTIST || it.explicitType == SearchItemType.PROFILE }
    val allAlbums    = regularItems.filter { it.explicitType == SearchItemType.ALBUM }
    val allVideos    = regularItems.filter { it.explicitType == SearchItemType.VIDEO }
    val allPlaylists = regularItems.filter {
        it.explicitType == SearchItemType.PLAYLIST ||
                it.explicitType == SearchItemType.EPISODE  ||
                it.explicitType == SearchItemType.PODCAST
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 200.dp)
    ) {
        // Top match section
        if (topMatch != null) {
            item {
                ResultSectionHeader(title = stringResource(R.string.search_best_result))
            }
            item {
                TopMatchCard(
                    item = topMatch,
                    onClick = { onItemClick(topMatch) }
                )
            }
        }

        // Songs section
        if (allSongs.isNotEmpty()) {
            item {
                ResultSectionHeader(title = stringResource(R.string.search_category_songs))
            }
            items(items = allSongs, key = { "song_${it.id}" }) { song ->
                SongResultItem(item = song, onClick = { onItemClick(song) })
            }
            item {
                when {
                    SearchCategory.SONGS in reachedEndOfCategory -> SectionEndIndicator()
                    loadingCategory == SearchCategory.SONGS -> LoadingMoreIndicator()
                    else -> ExpandSectionButton(onClick = { onFetchMoreCategory?.invoke(SearchCategory.SONGS) })
                }
            }
        }

        // Artists section
        if (allArtists.isNotEmpty()) {
            item {
                ResultSectionHeader(title = stringResource(R.string.search_category_artists))
            }
            items(items = allArtists, key = { "artist_${it.id}" }) { artist ->
                ArtistResultItem(item = artist, onClick = { onItemClick(artist) })
            }
            item {
                when {
                    SearchCategory.ARTISTS in reachedEndOfCategory -> SectionEndIndicator()
                    loadingCategory == SearchCategory.ARTISTS -> LoadingMoreIndicator()
                    else -> ExpandSectionButton(onClick = { onFetchMoreCategory?.invoke(SearchCategory.ARTISTS) })
                }
            }
        }

        // Albums section
        if (allAlbums.isNotEmpty()) {
            item {
                ResultSectionHeader(title = stringResource(R.string.search_category_albums))
            }
            items(items = allAlbums, key = { "album_${it.id}" }) { album ->
                AlbumResultItem(item = album, onClick = { onItemClick(album) })
            }
            item {
                when {
                    SearchCategory.ALBUMS in reachedEndOfCategory -> SectionEndIndicator()
                    loadingCategory == SearchCategory.ALBUMS -> LoadingMoreIndicator()
                    else -> ExpandSectionButton(onClick = { onFetchMoreCategory?.invoke(SearchCategory.ALBUMS) })
                }
            }
        }

        // Videos section
        if (allVideos.isNotEmpty()) {
            item {
                ResultSectionHeader(title = stringResource(R.string.search_category_videos))
            }
            items(items = allVideos, key = { "video_${it.id}" }) { video ->
                VideoResultItem(item = video, onClick = { onItemClick(video) })
            }
            item {
                when {
                    SearchCategory.VIDEOS in reachedEndOfCategory -> SectionEndIndicator()
                    loadingCategory == SearchCategory.VIDEOS -> LoadingMoreIndicator()
                    else -> ExpandSectionButton(onClick = { onFetchMoreCategory?.invoke(SearchCategory.VIDEOS) })
                }
            }
        }

        // Playlists section
        if (allPlaylists.isNotEmpty()) {
            item {
                ResultSectionHeader(title = stringResource(R.string.search_section_playlists))
            }
            items(items = allPlaylists, key = { "pl_${it.id}" }) { pl ->
                PlaylistResultItem(item = pl, onClick = { onItemClick(pl) })
            }
            item {
                when {
                    SearchCategory.COMMUNITY_PLAYLISTS in reachedEndOfCategory -> SectionEndIndicator()
                    loadingCategory == SearchCategory.COMMUNITY_PLAYLISTS -> LoadingMoreIndicator()
                    else -> ExpandSectionButton(onClick = { onFetchMoreCategory?.invoke(SearchCategory.COMMUNITY_PLAYLISTS) })
                }
            }
        }
    }
}

// Section header title
@Composable
private fun ResultSectionHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
}

// Expand section button
@Composable
private fun ExpandSectionButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.KeyboardArrowDown,
            contentDescription = "Expandir seção",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(24.dp)
        )
    }
}

// End of section indicator label
@Composable
private fun SectionEndIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.search_section_end),
            fontFamily = Poppins,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
    }
}

// Loading indicator container
@Composable
private fun LoadingMoreIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(28.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

// Search empty state layout
@Composable
fun SearchEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.SearchOff,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier.size(48.dp)
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.search_empty_state),
            fontFamily = Poppins,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}
