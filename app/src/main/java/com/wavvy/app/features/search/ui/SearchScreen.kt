package com.wavvy.app.features.search.ui

// Activity and Compose foundation
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
// State and UI
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// Project components
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import com.wavvy.app.R
import com.wavvy.app.core.designsystem.components.SearchCategory
import com.wavvy.app.features.home.ui.PlayerState
import com.wavvy.app.features.search.data.SearchHistoryManager
import com.wavvy.app.features.search.ui.components.*
import kotlinx.coroutines.launch

// Main search feature coordinator
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    playerState: PlayerState,
    onNavigateBack: () -> Unit = {},
    viewModel: SearchViewModel = koinViewModel()
) {
    var activeQuery by rememberSaveable { mutableStateOf("") }
    var isResultsVisible by rememberSaveable { mutableStateOf(false) }

    val suggestions by viewModel.suggestions.collectAsState()
    val isFetchingSuggestions by viewModel.isFetchingSuggestions.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val historyManager = remember { SearchHistoryManager(context) }

    val handleBackAction = {
        keyboardController?.hide()
        focusManager.clearFocus()
        if (isResultsVisible) {
            isResultsVisible = false
            activeQuery = ""
            viewModel.clearSearch()
        } else {
            onNavigateBack()
        }
    }

    // Perform a search and save to history
    val performAndSave: (String) -> Unit = { query ->
        activeQuery = query
        isResultsVisible = true
        keyboardController?.hide()
        focusManager.clearFocus()
        scope.launch { historyManager.saveSearch(query) }
        viewModel.performSearch(query, SearchCategory.ALL)
    }

    BackHandler(enabled = !playerState.isPlayerExpanded, onBack = handleBackAction)

    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(
            isResultsVisible = isResultsVisible,
            externalQuery = activeQuery,
            onSearch = { query -> performAndSave(query) },
            onBack = handleBackAction,
            onQueryChange = { query ->
                activeQuery = query
                if (query.isEmpty()) {
                    isResultsVisible = false
                    viewModel.clearSearch()
                } else {
                    viewModel.onQueryChanged(query)
                }
            },
            onOpenSearchHome = {
                isResultsVisible = false
                if (activeQuery.isNotBlank()) {
                    viewModel.restoreSuggestions()
                }
            }
        )

        AnimatedContent(
            targetState = isResultsVisible,
            transitionSpec = {
                fadeIn(animationSpec = tween(180)) togetherWith
                        fadeOut(animationSpec = tween(180))
            },
            label = "search_content_transition",
            modifier = Modifier.fillMaxSize()
        ) { showResults ->
            if (showResults) {
                SearchResultScreen(
                    query = activeQuery,
                    playerState = playerState,
                    viewModel = viewModel
                )
            } else {
                // Animated crossfade when suggestions list updates
                AnimatedContent(
                    targetState = if (activeQuery.isNotBlank()) suggestions else emptyList(),
                    transitionSpec = {
                        fadeIn(animationSpec = tween(160)) togetherWith fadeOut(animationSpec = tween(160))
                    },
                    label = "suggestions_list_crossfade",
                    modifier = Modifier.fillMaxSize()
                ) { list ->
                    when {
                        activeQuery.isNotBlank() && list.isNotEmpty() -> {
                            val listState = rememberLazyListState()
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(top = 8.dp, bottom = 140.dp)
                            ) {
                                items(
                                    items = list,
                                    key = { it.id }
                                ) { sug ->
                                    val rowModifier = Modifier.animateItem()

                                    if (sug.explicitType == SearchItemType.UNKNOWN) {
                                        SearchTextSuggestionItem(
                                            queryText = sug.title,
                                            onClick = { performAndSave(sug.title) },
                                            onInsertClick = {
                                                activeQuery = sug.title
                                                viewModel.onQueryChanged(sug.title)
                                            },
                                            modifier = rowModifier
                                        )
                                    } else {
                                        SearchResultRow(
                                            title = sug.title,
                                            subtitle = sug.subtitle,
                                            imageUrl = sug.imageUrl,
                                            isCircle = sug.explicitType == SearchItemType.ARTIST,
                                            onClick = {
                                                if (!sug.videoId.isNullOrBlank()) {
                                                    val artist = sug.subtitle ?: ""
                                                    scope.launch { historyManager.saveSearch(sug.title) }
                                                    playerState.updatePlayback(
                                                        title = sug.title,
                                                        artists = listOf(artist),
                                                        imageUrl = sug.imageUrl,
                                                        url = sug.videoId
                                                    )
                                                } else {
                                                    performAndSave(sug.title)
                                                }
                                            },
                                            modifier = rowModifier
                                        )
                                    }
                                }
                            }
                        }

                        // Query typed but fetch finished and returned nothing
                        activeQuery.isNotBlank() && !isFetchingSuggestions && list.isEmpty() -> {
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
                                    modifier = Modifier.size(56.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = stringResource(R.string.search_no_suggestions),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        // Still fetching or query is empty
                        else -> Box(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}
