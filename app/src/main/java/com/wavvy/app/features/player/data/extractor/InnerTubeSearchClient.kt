package com.wavvy.app.features.player.data.extractor

// JSON parsing
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
// Network client
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit
// Domain models
import com.wavvy.app.core.designsystem.components.SearchCategory
import com.wavvy.app.features.search.ui.components.SearchItemType
import com.wavvy.app.features.search.ui.components.SearchResultData

// Search result before matching against a chart entry
data class InnerTubeSearchResult(
    val videoId: String,
    val title: String,
    val artist: String?
)

// Search response container with continuation token for pagination
data class SearchResponse(
    val results: List<SearchResultData>,
    val continuationToken: String?
)

// Resolves a text query into a videoId using the YT Music search endpoint
object InnerTubeSearchClient {

    private val gson = Gson()
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    // Search filter params per category
    private const val SONGS_FILTER_PARAM = "EgWKAQIIAWoKEAkQBRAKEAMQBA=="
    private const val VIDEOS_FILTER_PARAM = "EgWKAQIQAWoKEAkQChAFEAMQBA=="
    private const val ALBUMS_FILTER_PARAM = "EgWKAQIYAWoKEAkQChAFEAMQBA=="
    private const val ARTISTS_FILTER_PARAM = "EgWKAQIgAWoKEAkQChAFEAMQBA=="
    private const val COMMUNITY_PLAYLISTS_PARAM = "EgeKAQQoAEABagoQAxAEEAoQCRAF"
    private const val FEATURED_PLAYLISTS_PARAM = "EgeKAQQoADgBagwQDhAKEAMQBRAJEAQ="
    private const val EPISODES_PARAM = "EgWKAQJIAWoKEAkQChAFEAMQBA=="
    private const val PODCASTS_PARAM = "EgWKAQJQAWoKEAkQChAFEAMQBA=="
    private const val PROFILES_PARAM = "EgWKAQJYAWoSEAUQCRADEAQQEBAVEAoQDhAR"

    private fun buildSearchPayload(query: String, params: String? = SONGS_FILTER_PARAM): String {
        val root = JsonObject()

        val context = JsonObject()
        val clientObj = JsonObject()
        clientObj.addProperty("clientName", "WEB_REMIX")
        clientObj.addProperty("clientVersion", "1.20240501.01.00")
        clientObj.addProperty("hl", "pt-BR")
        clientObj.addProperty("gl", "BR")
        context.add("client", clientObj)
        root.add("context", context)

        root.addProperty("query", query)
        if (!params.isNullOrEmpty()) {
            root.addProperty("params", params)
        }

        return gson.toJson(root)
    }

    private fun buildContinuationPayload(): String {
        val root = JsonObject()
        val context = JsonObject()
        val clientObj = JsonObject()
        clientObj.addProperty("clientName", "WEB_REMIX")
        clientObj.addProperty("clientVersion", "1.20240501.01.00")
        clientObj.addProperty("hl", "pt-BR")
        clientObj.addProperty("gl", "BR")
        context.add("client", clientObj)
        root.add("context", context)
        return gson.toJson(root)
    }

    // Full search across all categories returning rich SearchResultData and continuation token
    fun searchFull(query: String, category: SearchCategory = SearchCategory.ALL): SearchResponse {
        try {
            val url = "https://music.youtube.com/youtubei/v1/search?prettyPrint=false"
            val filterParam = when (category) {
                SearchCategory.ALL -> null
                SearchCategory.ARTISTS -> ARTISTS_FILTER_PARAM
                SearchCategory.SONGS -> SONGS_FILTER_PARAM
                SearchCategory.VIDEOS -> VIDEOS_FILTER_PARAM
                SearchCategory.COMMUNITY_PLAYLISTS -> COMMUNITY_PLAYLISTS_PARAM
                SearchCategory.EPISODES -> EPISODES_PARAM
                SearchCategory.ALBUMS -> ALBUMS_FILTER_PARAM
                SearchCategory.PROFILES -> PROFILES_PARAM
                SearchCategory.FEATURED_PLAYLISTS -> FEATURED_PLAYLISTS_PARAM
                SearchCategory.PODCASTS -> PODCASTS_PARAM
            }

            val response = executeSearchRequest(url, buildSearchPayload(query, filterParam), category)
            if (category == SearchCategory.COMMUNITY_PLAYLISTS && response.results.isEmpty()) {
                // Fallback to FEATURED_PLAYLISTS_PARAM if COMMUNITY_PLAYLISTS returns 0 items
                return executeSearchRequest(url, buildSearchPayload(query, FEATURED_PLAYLISTS_PARAM), category)
            }
            return response
        } catch (_: Exception) {
            return SearchResponse(emptyList(), null)
        }
    }

    private fun executeSearchRequest(url: String, jsonBody: String, category: SearchCategory): SearchResponse {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonBody.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
            .addHeader("Content-Type", "application/json")
            .addHeader("X-Origin", "https://music.youtube.com")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) return SearchResponse(emptyList(), null)
            val responseBody = response.body.string()
            val rootObj = JsonParser.parseString(responseBody).asJsonObject
            return parseFullSearchResults(rootObj, category)
        }
    }

    // Fetch continuation results for infinite scrolling
    fun searchContinuation(continuationToken: String, defaultCategory: SearchCategory = SearchCategory.ALL): SearchResponse {
        try {
            val url = "https://music.youtube.com/youtubei/v1/search?continuation=$continuationToken&prettyPrint=false"
            val jsonBody = buildContinuationPayload()
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = jsonBody.toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .post(body)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Origin", "https://music.youtube.com")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return SearchResponse(emptyList(), null)
                val responseBody = response.body.string()
                val rootObj = JsonParser.parseString(responseBody).asJsonObject
                return parseContinuationResults(rootObj, defaultCategory)
            }
        } catch (_: Exception) {
            return SearchResponse(emptyList(), null)
        }
    }

    // Search and return the raw ordered list of song results
    fun search(query: String): List<InnerTubeSearchResult> {
        try {
            val url = "https://music.youtube.com/youtubei/v1/search?prettyPrint=false"
            val jsonBody = buildSearchPayload(query, SONGS_FILTER_PARAM)
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = jsonBody.toRequestBody(mediaType)

            val request = Request.Builder()
                .url(url)
                .post(body)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Origin", "https://music.youtube.com")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return emptyList()
                val responseBody = response.body.string()
                val rootObj = JsonParser.parseString(responseBody).asJsonObject
                return parseSearchResults(rootObj)
            }
        } catch (_: Exception) {
            return emptyList()
        }
    }

    // Resolve a chart entry into a videoId, preferring an artist name match
    fun resolveVideoId(title: String, artist: String?): String? {
        val query = if (artist.isNullOrBlank()) title else "$artist $title"
        val results = search(query)
        if (results.isEmpty()) return null

        if (!artist.isNullOrBlank()) {
            val normalizedArtist = artist.lowercase().trim()
            val matched = results.firstOrNull { result ->
                result.artist?.lowercase()?.contains(normalizedArtist) == true ||
                        normalizedArtist.contains(result.artist?.lowercase().orEmpty())
            }
            if (matched != null) return matched.videoId
        }

        return results.first().videoId
    }

    // Fetches live autocomplete search suggestions
    fun getSearchSuggestions(query: String): List<SearchResultData> {
        if (query.isBlank()) return emptyList()
        try {
            val url = "https://music.youtube.com/youtubei/v1/music/get_search_suggestions?prettyPrint=false"
            val root = JsonObject()
            val context = JsonObject()
            val clientObj = JsonObject()
            clientObj.addProperty("clientName", "WEB_REMIX")
            clientObj.addProperty("clientVersion", "1.20240501.01.00")
            clientObj.addProperty("hl", "pt-BR")
            clientObj.addProperty("gl", "BR")
            context.add("client", clientObj)
            root.add("context", context)
            root.addProperty("input", query)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = gson.toJson(root).toRequestBody(mediaType)
            val request = Request.Builder()
                .url(url)
                .post(body)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .addHeader("Content-Type", "application/json")
                .addHeader("X-Origin", "https://music.youtube.com")
                .build()

            val results = mutableListOf<SearchResultData>()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return emptyList()
                val responseBody = response.body.string()
                val rootObj = JsonParser.parseString(responseBody).asJsonObject
                val contents = rootObj.getAsJsonArray("contents") ?: return emptyList()

                for (i in 0 until contents.size()) {
                    val ssr = contents.get(i).asJsonObject.getAsJsonObject("searchSuggestionsSectionRenderer") ?: continue
                    val items = ssr.getAsJsonArray("contents") ?: continue

                    for (j in 0 until items.size()) {
                        val itemObj = items.get(j).asJsonObject

                        // Text autocomplete suggestion
                        val sr = itemObj.getAsJsonObject("searchSuggestionRenderer")
                        if (sr != null) {
                            val runs = sr.getAsJsonObject("suggestion")?.getAsJsonArray("runs")
                            val text = runs?.joinToString("") { runElem ->
                                runElem.asJsonObject.get("text")?.asString ?: ""
                            }
                            if (!text.isNullOrBlank()) {
                                results.add(
                                    SearchResultData(
                                        id = "sug_${i}_${j}_$text",
                                        title = text,
                                        subtitle = null,
                                        explicitType = SearchItemType.UNKNOWN
                                    )
                                )
                            }
                        }

                        // Rich item suggestion (song/artist/album)
                        val mr = itemObj.getAsJsonObject("musicResponsiveListItemRenderer")
                        if (mr != null) {
                            parseFullResponsiveItem(mr, SearchCategory.ALL)?.let {
                                results.add(it.copy(id = "rich_${i}_${j}_${it.id}"))
                            }
                        }
                    }
                }
            }
            return results
        } catch (_: Exception) {
            return emptyList()
        }
    }

    private fun extractCleanSubtitle(item: JsonObject): String? {
        val flexColumns = item.getAsJsonArray("flexColumns") ?: return null
        if (flexColumns.size() < 2) return null

        val typeWords = setOf("música", "music", "vídeo", "video", "single", "álbum", "album", "ep", "artista", "artist", "playlist", "podcast", "episódio", "episode", "perfil", "profile")

        val artists = mutableListOf<String>()
        var duration: String? = null
        val fallbackRuns = mutableListOf<String>()

        for (colIdx in 1 until flexColumns.size()) {
            val runs = flexColumns.get(colIdx).asJsonObject
                .getAsJsonObject("musicResponsiveListItemFlexColumnRenderer")
                ?.getAsJsonObject("text")
                ?.getAsJsonArray("runs") ?: continue

            for (r in 0 until runs.size()) {
                val runObj = runs.get(r).asJsonObject
                val txt = runObj.get("text")?.asString.orEmpty().trim()
                if (txt.isBlank() || txt == "•" || txt == "·") continue

                val lower = txt.lowercase()
                if (lower in typeWords) continue
                if (txt.matches(Regex("""^\d{4}$"""))) continue
                if (lower.contains("visualiza") || lower.contains("tocou") || lower.contains("ouvintes") ||
                    lower.contains("inscrito") || lower.contains("subscriber") || lower.contains("views") ||
                    lower.contains("plays")) continue

                if (txt.matches(Regex("""^\d+:\d{2}(:\d{2})?$"""))) {
                    duration = txt
                    continue
                }

                val pageType = runObj.getAsJsonObject("navigationEndpoint")
                    ?.getAsJsonObject("browseEndpoint")
                    ?.getAsJsonObject("browseEndpointContextSupportedConfigs")
                    ?.getAsJsonObject("browseEndpointContextMusicConfig")
                    ?.get("pageType")?.asString

                if (pageType == "MUSIC_PAGE_TYPE_ARTIST" || pageType == "MUSIC_PAGE_TYPE_USER_CHANNEL") {
                    artists.add(txt)
                } else {
                    fallbackRuns.add(txt)
                }
            }
        }

        val mainText = when {
            artists.isNotEmpty() -> artists.distinct().joinToString(" e ")
            fallbackRuns.isNotEmpty() -> fallbackRuns.first()
            else -> ""
        }

        return when {
            mainText.isNotBlank() && duration != null -> "$mainText • $duration"
            mainText.isNotBlank() -> mainText
            duration != null -> duration
            else -> null
        }
    }

    private fun cleanRunsToSubtitle(runs: com.google.gson.JsonArray?): String? {
        if (runs == null || runs.size() == 0) return null
        val typeWords = setOf("música", "music", "vídeo", "video", "single", "álbum", "album", "ep", "artista", "artist", "playlist", "podcast", "episódio", "episode", "perfil", "profile")

        val artists = mutableListOf<String>()
        var duration: String? = null
        val fallbackRuns = mutableListOf<String>()

        for (r in 0 until runs.size()) {
            val runObj = runs.get(r).asJsonObject
            val txt = runObj.get("text")?.asString.orEmpty().trim()
            if (txt.isBlank() || txt == "•" || txt == "·") continue

            val lower = txt.lowercase()
            if (lower in typeWords) continue
            if (txt.matches(Regex("""^\d{4}$"""))) continue
            if (lower.contains("visualiza") || lower.contains("tocou") || lower.contains("ouvintes") ||
                lower.contains("inscrito") || lower.contains("subscriber") || lower.contains("views") ||
                lower.contains("plays")) continue

            if (txt.matches(Regex("""^\d+:\d{2}(:\d{2})?$"""))) {
                duration = txt
                continue
            }

            val pageType = runObj.getAsJsonObject("navigationEndpoint")
                ?.getAsJsonObject("browseEndpoint")
                ?.getAsJsonObject("browseEndpointContextSupportedConfigs")
                ?.getAsJsonObject("browseEndpointContextMusicConfig")
                ?.get("pageType")?.asString

            if (pageType == "MUSIC_PAGE_TYPE_ARTIST" || pageType == "MUSIC_PAGE_TYPE_USER_CHANNEL") {
                artists.add(txt)
            } else {
                fallbackRuns.add(txt)
            }
        }

        val mainText = when {
            artists.isNotEmpty() -> artists.distinct().joinToString(" e ")
            fallbackRuns.isNotEmpty() -> fallbackRuns.first()
            else -> ""
        }

        return when {
            mainText.isNotBlank() && duration != null -> "$mainText • $duration"
            mainText.isNotBlank() -> mainText
            duration != null -> duration
            else -> null
        }
    }

    private fun parseFullSearchResults(rootObj: JsonObject, defaultCategory: SearchCategory): SearchResponse {
        val results = mutableListOf<SearchResultData>()
        var continuationToken: String? = null

        val sectionList = rootObj
            .getAsJsonObject("contents")
            ?.getAsJsonObject("tabbedSearchResultsRenderer")
            ?.getAsJsonArray("tabs")
            ?.get(0)?.asJsonObject
            ?.getAsJsonObject("tabRenderer")
            ?.getAsJsonObject("content")
            ?.getAsJsonObject("sectionListRenderer")

        val contents = sectionList?.getAsJsonArray("contents") ?: return SearchResponse(emptyList(), null)

        for (i in 0 until contents.size()) {
            val section = contents.get(i).asJsonObject

            // Check for musicCardShelfRenderer
            val cardShelf = section.getAsJsonObject("musicCardShelfRenderer")
            if (cardShelf != null) {
                parseCardShelf(cardShelf)?.let { results.add(it) }
            }

            // Check for musicShelfRenderer
            val musicShelf = section.getAsJsonObject("musicShelfRenderer")
            if (musicShelf != null) {
                val items = musicShelf.getAsJsonArray("contents")
                if (items != null) {
                    for (j in 0 until items.size()) {
                        val itemObj = items.get(j).asJsonObject
                        val responsiveItem = itemObj.getAsJsonObject("musicResponsiveListItemRenderer") ?: continue
                        parseFullResponsiveItem(responsiveItem, defaultCategory)?.let { results.add(it) }
                    }
                }
                if (continuationToken == null) {
                    continuationToken = musicShelf.getAsJsonArray("continuations")
                        ?.get(0)?.asJsonObject
                        ?.getAsJsonObject("nextContinuationData")
                        ?.get("continuation")?.asString
                }
            }

            // Check for itemSectionRenderer
            val itemSection = section.getAsJsonObject("itemSectionRenderer")
            if (itemSection != null) {
                val items = itemSection.getAsJsonArray("contents")
                if (items != null) {
                    for (j in 0 until items.size()) {
                        val itemObj = items.get(j).asJsonObject
                        val responsiveItem = itemObj.getAsJsonObject("musicResponsiveListItemRenderer") ?: continue
                        parseFullResponsiveItem(responsiveItem, defaultCategory)?.let { results.add(it) }
                    }
                }
            }
        }

        if (continuationToken == null) {
            continuationToken = sectionList.getAsJsonArray("continuations")
                ?.get(0)?.asJsonObject
                ?.getAsJsonObject("nextContinuationData")
                ?.get("continuation")?.asString
        }

        return SearchResponse(results, continuationToken)
    }

    private fun parseContinuationResults(rootObj: JsonObject, defaultCategory: SearchCategory): SearchResponse {
        val results = mutableListOf<SearchResultData>()
        var nextContinuationToken: String? = null

        val cc = rootObj.getAsJsonObject("continuationContents")?.getAsJsonObject("musicShelfContinuation")
        if (cc != null) {
            val items = cc.getAsJsonArray("contents")
            if (items != null) {
                for (j in 0 until items.size()) {
                    val itemObj = items.get(j).asJsonObject
                    val responsiveItem = itemObj.getAsJsonObject("musicResponsiveListItemRenderer") ?: continue
                    parseFullResponsiveItem(responsiveItem, defaultCategory)?.let { results.add(it) }
                }
            }
            nextContinuationToken = cc.getAsJsonArray("continuations")
                ?.get(0)?.asJsonObject
                ?.getAsJsonObject("nextContinuationData")
                ?.get("continuation")?.asString
        }

        return SearchResponse(results, nextContinuationToken)
    }

    private fun parseCardShelf(card: JsonObject): SearchResultData? {
        val titleRuns = card.getAsJsonObject("title")?.getAsJsonArray("runs")
        val title = titleRuns?.get(0)?.asJsonObject?.get("text")?.asString ?: return null
        val subtitleRuns = card.getAsJsonObject("subtitle")?.getAsJsonArray("runs")
        val subtitle = cleanRunsToSubtitle(subtitleRuns)

        val thumbnails = card.getAsJsonObject("thumbnail")
            ?.getAsJsonObject("musicThumbnailRenderer")
            ?.getAsJsonObject("thumbnail")
            ?.getAsJsonArray("thumbnails")
        val imageUrl = if (thumbnails != null && thumbnails.size() > 0) {
            thumbnails.get(thumbnails.size() - 1).asJsonObject.get("url")?.asString
        } else null

        val thumbnailCrop = card.getAsJsonObject("thumbnail")
            ?.getAsJsonObject("musicThumbnailRenderer")
            ?.get("thumbnailCrop")?.asString.orEmpty()

        // onTap gives us the primary navigation
        val onTap = card.getAsJsonObject("onTap")
        val onTapVideoId = onTap?.getAsJsonObject("watchEndpoint")?.get("videoId")?.asString
        val onTapBrowse = onTap?.getAsJsonObject("browseEndpoint")
        val onTapBrowseId = onTapBrowse?.get("browseId")?.asString
        val onTapPageType = onTapBrowse
            ?.getAsJsonObject("browseEndpointContextSupportedConfigs")
            ?.getAsJsonObject("browseEndpointContextMusicConfig")
            ?.get("pageType")?.asString

        // Also check buttons for a playable videoId
        var buttonVideoId: String? = null
        val buttons = card.getAsJsonArray("buttons")
        if (buttons != null) {
            for (bIdx in 0 until buttons.size()) {
                val bRenderer = buttons.get(bIdx).asJsonObject.getAsJsonObject("buttonRenderer")
                val vid = bRenderer?.getAsJsonObject("command")?.getAsJsonObject("watchEndpoint")?.get("videoId")?.asString
                if (!vid.isNullOrBlank()) { buttonVideoId = vid; break }
            }
        }

        val videoId = onTapVideoId ?: buttonVideoId
        val browseId = onTapBrowseId

        val explicitType = when {
            onTapPageType == "MUSIC_PAGE_TYPE_ARTIST" || thumbnailCrop.contains("CIRCLE", ignoreCase = true) -> SearchItemType.ARTIST
            onTapPageType == "MUSIC_PAGE_TYPE_ALBUM" -> SearchItemType.ALBUM
            onTapPageType == "MUSIC_PAGE_TYPE_PLAYLIST" -> SearchItemType.PLAYLIST
            onTapPageType == "MUSIC_PAGE_TYPE_PODCAST_SHOW_DETAIL_PAGE" -> SearchItemType.PODCAST
            onTapPageType == "MUSIC_PAGE_TYPE_PODCAST_EPISODE_DETAIL_PAGE" -> SearchItemType.EPISODE
            videoId != null -> SearchItemType.SONG
            else -> SearchItemType.UNKNOWN
        }
        val isArtist = explicitType == SearchItemType.ARTIST

        return SearchResultData(
            id = videoId ?: browseId ?: ("card_$title"),
            title = title,
            subtitle = subtitle,
            imageUrl = imageUrl,
            isArtist = isArtist,
            videoId = videoId,
            browseId = browseId,
            category = if (isArtist) SearchCategory.ARTISTS else SearchCategory.SONGS,
            explicitType = explicitType,
            isTopMatch = true
        )
    }

    private fun parseFullResponsiveItem(item: JsonObject, defaultCategory: SearchCategory): SearchResultData? {
        val flexColumns = item.getAsJsonArray("flexColumns") ?: return null
        if (flexColumns.size() < 1) return null

        val col0 = flexColumns.get(0).asJsonObject
            .getAsJsonObject("musicResponsiveListItemFlexColumnRenderer")
            ?.getAsJsonObject("text")
            ?.getAsJsonArray("runs")
        val title = col0?.get(0)?.asJsonObject?.get("text")?.asString ?: return null

        val col0VideoId = col0.get(0)?.asJsonObject
            ?.getAsJsonObject("navigationEndpoint")
            ?.getAsJsonObject("watchEndpoint")
            ?.get("videoId")?.asString

        val subtitle: String? = extractCleanSubtitle(item)

        val thumbnails = item.getAsJsonObject("thumbnail")
            ?.getAsJsonObject("musicThumbnailRenderer")
            ?.getAsJsonObject("thumbnail")
            ?.getAsJsonArray("thumbnails")
        val imageUrl = if (thumbnails != null && thumbnails.size() > 0) {
            thumbnails.get(thumbnails.size() - 1).asJsonObject.get("url")?.asString
        } else null

        val thumbnailCrop = item.getAsJsonObject("thumbnail")
            ?.getAsJsonObject("musicThumbnailRenderer")
            ?.get("thumbnailCrop")?.asString.orEmpty()

        val itemNavEndpoint = item.getAsJsonObject("navigationEndpoint")
        val itemBrowseEndpoint = itemNavEndpoint?.getAsJsonObject("browseEndpoint")
        val itemBrowseId = itemBrowseEndpoint?.get("browseId")?.asString
        val itemPageType = itemBrowseEndpoint
            ?.getAsJsonObject("browseEndpointContextSupportedConfigs")
            ?.getAsJsonObject("browseEndpointContextMusicConfig")
            ?.get("pageType")?.asString

        val videoId = item.getAsJsonObject("playlistItemData")?.get("videoId")?.asString
            ?: col0VideoId
            ?: item.getAsJsonObject("overlay")
                ?.getAsJsonObject("musicItemThumbnailOverlayRenderer")
                ?.getAsJsonObject("content")
                ?.getAsJsonObject("musicPlayButtonRenderer")
                ?.getAsJsonObject("playNavigationEndpoint")
                ?.getAsJsonObject("watchEndpoint")
                ?.get("videoId")?.asString

        val browseId = itemBrowseId
        val id = videoId ?: browseId ?: "${title}_${subtitle.orEmpty()}"

        val explicitType: SearchItemType = when {
            itemPageType == "MUSIC_PAGE_TYPE_ARTIST" -> SearchItemType.ARTIST
            itemPageType == "MUSIC_PAGE_TYPE_ALBUM" -> SearchItemType.ALBUM
            itemPageType == "MUSIC_PAGE_TYPE_PLAYLIST" -> SearchItemType.PLAYLIST
            itemPageType == "MUSIC_PAGE_TYPE_PODCAST_SHOW_DETAIL_PAGE" -> SearchItemType.PODCAST
            itemPageType == "MUSIC_PAGE_TYPE_PODCAST_EPISODE_DETAIL_PAGE" -> SearchItemType.EPISODE
            itemPageType == "MUSIC_PAGE_TYPE_USER_CHANNEL" -> SearchItemType.PROFILE
            thumbnailCrop.contains("CIRCLE", ignoreCase = true) -> SearchItemType.ARTIST
            videoId != null -> SearchItemType.SONG
            defaultCategory == SearchCategory.ARTISTS -> SearchItemType.ARTIST
            defaultCategory == SearchCategory.ALBUMS -> SearchItemType.ALBUM
            defaultCategory == SearchCategory.VIDEOS -> SearchItemType.VIDEO
            defaultCategory == SearchCategory.EPISODES -> SearchItemType.EPISODE
            defaultCategory == SearchCategory.PODCASTS -> SearchItemType.PODCAST
            defaultCategory == SearchCategory.COMMUNITY_PLAYLISTS || defaultCategory == SearchCategory.FEATURED_PLAYLISTS -> SearchItemType.PLAYLIST
            defaultCategory == SearchCategory.PROFILES -> SearchItemType.PROFILE
            else -> SearchItemType.SONG
        }

        val isArtist = explicitType == SearchItemType.ARTIST || explicitType == SearchItemType.PROFILE

        val itemCategory = when (explicitType) {
            SearchItemType.ARTIST, SearchItemType.PROFILE -> SearchCategory.ARTISTS
            SearchItemType.ALBUM -> SearchCategory.ALBUMS
            SearchItemType.VIDEO -> SearchCategory.VIDEOS
            SearchItemType.PLAYLIST -> SearchCategory.COMMUNITY_PLAYLISTS
            SearchItemType.EPISODE -> SearchCategory.EPISODES
            SearchItemType.PODCAST -> SearchCategory.PODCASTS
            else -> defaultCategory
        }

        return SearchResultData(
            id = id,
            title = title,
            subtitle = subtitle,
            imageUrl = imageUrl,
            isArtist = isArtist,
            videoId = videoId,
            browseId = browseId,
            category = itemCategory,
            explicitType = explicitType
        )
    }

    private fun parseSearchResults(rootObj: JsonObject): List<InnerTubeSearchResult> {
        val results = mutableListOf<InnerTubeSearchResult>()

        val contents = rootObj
            .getAsJsonObject("contents")
            ?.getAsJsonObject("tabbedSearchResultsRenderer")
            ?.getAsJsonArray("tabs")
            ?.get(0)?.asJsonObject
            ?.getAsJsonObject("tabRenderer")
            ?.getAsJsonObject("content")
            ?.getAsJsonObject("sectionListRenderer")
            ?.getAsJsonArray("contents") ?: return emptyList()

        for (i in 0 until contents.size()) {
            val section = contents.get(i).asJsonObject
            val musicShelf = section.getAsJsonObject("musicShelfRenderer") ?: continue
            val items = musicShelf.getAsJsonArray("contents") ?: continue

            for (j in 0 until items.size()) {
                val itemObj = items.get(j).asJsonObject
                val responsiveItem = itemObj.getAsJsonObject("musicResponsiveListItemRenderer") ?: continue
                parseResponsiveItem(responsiveItem)?.let { results.add(it) }
            }
        }

        return results
    }

    private fun parseResponsiveItem(item: JsonObject): InnerTubeSearchResult? {
        val videoId = item.getAsJsonObject("playlistItemData")
            ?.get("videoId")?.asString
            ?: item.getAsJsonArray("flexColumns")
                ?.get(0)?.asJsonObject
                ?.getAsJsonObject("musicResponsiveListItemFlexColumnRenderer")
                ?.getAsJsonObject("text")
                ?.getAsJsonArray("runs")
                ?.get(0)?.asJsonObject
                ?.getAsJsonObject("navigationEndpoint")
                ?.getAsJsonObject("watchEndpoint")
                ?.get("videoId")?.asString

        if (videoId.isNullOrBlank()) return null

        val flexColumns = item.getAsJsonArray("flexColumns") ?: return null
        if (flexColumns.size() < 1) return null

        val titleRuns = flexColumns.get(0).asJsonObject
            .getAsJsonObject("musicResponsiveListItemFlexColumnRenderer")
            ?.getAsJsonObject("text")
            ?.getAsJsonArray("runs")
        val title = titleRuns?.get(0)?.asJsonObject?.get("text")?.asString ?: return null

        var artist: String? = null
        if (flexColumns.size() >= 2) {
            val subtitleRuns = flexColumns.get(1).asJsonObject
                .getAsJsonObject("musicResponsiveListItemFlexColumnRenderer")
                ?.getAsJsonObject("text")
                ?.getAsJsonArray("runs")

            artist = subtitleRuns?.get(0)?.asJsonObject?.get("text")?.asString
        }

        return InnerTubeSearchResult(
            videoId = videoId,
            title = title,
            artist = artist
        )
    }
}
