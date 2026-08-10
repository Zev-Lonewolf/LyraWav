package com.wavvy.app.features.search.ui

// Compose layouts and foundations
import androidx.compose.foundation.layout.*
// Material 3 components
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
// UI styling and utilities
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
// Project resources
import com.wavvy.app.core.designsystem.components.FilterChipsRow
import com.wavvy.app.core.designsystem.components.SearchCategory
import com.wavvy.app.features.home.ui.PlayerState
import com.wavvy.app.features.search.ui.components.SearchResultList

// Main search result screen layout
@Composable
fun SearchResultScreen(
    query: String,
    playerState: PlayerState,
    viewModel: SearchViewModel
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val loadingCategory by viewModel.loadingCategory.collectAsState()
    val reachedEndOfCategory by viewModel.reachedEndOfCategory.collectAsState()

    // Query update observer
    LaunchedEffect(query) {
        if (query.isNotBlank() && viewModel.query.value != query) {
            viewModel.performSearch(query, SearchCategory.ALL)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Filter category selection row
        FilterChipsRow(
            selectedCategory = selectedCategory,
            onCategorySelected = { viewModel.onCategorySelected(it) },
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        // Results container
        Box(modifier = Modifier.fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                SearchResultList(
                    category = selectedCategory,
                    results = searchResults,
                    isLoadingMore = isLoadingMore,
                    loadingCategory = loadingCategory,
                    reachedEndOfCategory = reachedEndOfCategory,
                    onLoadMore = { viewModel.loadMoreResults() },
                    onFetchMoreCategory = { viewModel.fetchMoreCategoryItems(it) },
                    onSeeAll = { category -> viewModel.onCategorySelected(category) },
                    onItemClick = { item ->
                        if (!item.videoId.isNullOrBlank()) {
                            val artist = item.subtitle
                                ?.split(" • ")
                                ?.getOrNull(0)
                                ?: item.subtitle ?: ""
                            playerState.updatePlayback(
                                title = item.title,
                                artists = listOf(artist),
                                imageUrl = item.imageUrl,
                                url = item.videoId
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
