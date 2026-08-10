package com.wavvy.app.features.search.ui

// Architecture and state management
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wavvy.app.core.designsystem.components.SearchCategory
import com.wavvy.app.features.player.data.extractor.InnerTubeSearchClient
import com.wavvy.app.features.search.ui.components.SearchResultData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.milliseconds

// View model managing search flow
class SearchViewModel : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _selectedCategory = MutableStateFlow(SearchCategory.ALL)
    val selectedCategory: StateFlow<SearchCategory> = _selectedCategory.asStateFlow()

    private val _searchResults = MutableStateFlow<List<SearchResultData>>(emptyList())
    val searchResults: StateFlow<List<SearchResultData>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    private val _suggestions = MutableStateFlow<List<SearchResultData>>(emptyList())
    val suggestions: StateFlow<List<SearchResultData>> = _suggestions.asStateFlow()

    private val _isFetchingSuggestions = MutableStateFlow(false)
    val isFetchingSuggestions: StateFlow<Boolean> = _isFetchingSuggestions.asStateFlow()

    private val _loadingCategory = MutableStateFlow<SearchCategory?>(null)
    val loadingCategory: StateFlow<SearchCategory?> = _loadingCategory.asStateFlow()

    private val _reachedEndOfCategory = MutableStateFlow<Set<SearchCategory>>(emptySet())
    val reachedEndOfCategory: StateFlow<Set<SearchCategory>> = _reachedEndOfCategory.asStateFlow()

    private var continuationToken: String? = null
    private val categoryTokens = mutableMapOf<SearchCategory, String?>()
    private var searchJob: Job? = null
    private var suggestionsJob: Job? = null

    // Query text change observer
    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
        suggestionsJob?.cancel()

        if (newQuery.isBlank()) {
            _suggestions.value = emptyList()
            _isFetchingSuggestions.value = false
        } else {
            _isFetchingSuggestions.value = true
            suggestionsJob = viewModelScope.launch {
                try {
                    kotlinx.coroutines.delay(30.milliseconds)
                    val sugList = withContext(Dispatchers.IO) {
                        try {
                            InnerTubeSearchClient.getSearchSuggestions(newQuery.trim())
                        } catch (_: Exception) {
                            null
                        }
                    }
                    if (sugList != null) {
                        _suggestions.value = sugList
                    }
                } catch (e: kotlinx.coroutines.CancellationException) {
                    throw e
                } catch (_: Exception) {
                } finally {
                    _isFetchingSuggestions.value = false
                }
            }
        }
    }

    // Category selection update handler
    fun onCategorySelected(category: SearchCategory) {
        if (_selectedCategory.value == category && _searchResults.value.isNotEmpty()) return
        _selectedCategory.value = category
        if (_query.value.isNotBlank()) {
            executeSearch(_query.value, category)
        }
    }

    // Direct search execution entry point
    fun performSearch(queryText: String = _query.value, category: SearchCategory? = null) {
        if (queryText.isBlank()) return
        val targetCategory = category ?: SearchCategory.ALL
        _query.value = queryText
        _selectedCategory.value = targetCategory
        suggestionsJob?.cancel()
        executeSearch(queryText, targetCategory)
    }

    // Internal full search execution
    private fun executeSearch(queryText: String, category: SearchCategory) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _isLoading.value = true
            _searchResults.value = emptyList()
            continuationToken = null

            val response = withContext(Dispatchers.IO) {
                InnerTubeSearchClient.searchFull(queryText.trim(), category)
            }
            _searchResults.value = response.results
            continuationToken = response.continuationToken
            categoryTokens.clear()
            _reachedEndOfCategory.value = emptySet()
            if (response.continuationToken != null) {
                categoryTokens[category] = response.continuationToken
            }
            _isLoading.value = false
        }
    }

    // Infinite scroll pagination loader
    fun loadMoreResults() {
        val currentToken = continuationToken ?: return
        if (_isLoadingMore.value || _isLoading.value) return

        viewModelScope.launch {
            _isLoadingMore.value = true
            val response = withContext(Dispatchers.IO) {
                InnerTubeSearchClient.searchContinuation(currentToken, _selectedCategory.value)
            }
            if (response.results.isNotEmpty()) {
                val currentList = _searchResults.value.toMutableList()
                val existingIds = currentList.map { it.id }.toSet()
                val newUniqueResults = response.results.filter { it.id !in existingIds }
                currentList.addAll(newUniqueResults)
                _searchResults.value = currentList
            }
            continuationToken = response.continuationToken
            _isLoadingMore.value = false
        }
    }

    // Search state reset handler
    fun clearSearch() {
        searchJob?.cancel()
        suggestionsJob?.cancel()
        _query.value = ""
        _selectedCategory.value = SearchCategory.ALL
        _searchResults.value = emptyList()
        _suggestions.value = emptyList()
        _isLoading.value = false
        _isLoadingMore.value = false
        continuationToken = null
        categoryTokens.clear()
        _reachedEndOfCategory.value = emptySet()
    }

    // Category specific pagination loader
    fun fetchMoreCategoryItems(category: SearchCategory) {
        val q = _query.value
        if (q.isBlank() || _loadingCategory.value != null) return

        viewModelScope.launch {
            _loadingCategory.value = category
            val existingToken = categoryTokens[category]
            val response = withContext(Dispatchers.IO) {
                if (existingToken != null) {
                    InnerTubeSearchClient.searchContinuation(existingToken, category)
                } else {
                    InnerTubeSearchClient.searchFull(q.trim(), category)
                }
            }
            categoryTokens[category] = response.continuationToken

            if (response.results.isEmpty() || response.continuationToken == null) {
                _reachedEndOfCategory.value += category
            }

            if (response.results.isNotEmpty()) {
                val currentList = _searchResults.value.toMutableList()
                val existingIds = currentList.map { it.id }.toSet()
                val newUniqueResults = response.results.filter { it.id !in existingIds }
                currentList.addAll(newUniqueResults)
                _searchResults.value = currentList
            }
            _loadingCategory.value = null
        }
    }

    // Instant suggestions restoration handler
    fun restoreSuggestions() {
        val q = _query.value
        if (q.isBlank()) return
        suggestionsJob?.cancel()
        suggestionsJob = viewModelScope.launch {
            try {
                val sugList = withContext(Dispatchers.IO) {
                    try {
                        InnerTubeSearchClient.getSearchSuggestions(q.trim())
                    } catch (_: Exception) {
                        null
                    }
                }
                if (sugList != null) {
                    _suggestions.value = sugList
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                throw e
            } catch (_: Exception) {
            }
        }
    }
}
