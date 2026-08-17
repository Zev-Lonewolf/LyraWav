package com.wavvy.app.core.designsystem.bottomsheet

// Compose runtime components
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

// Target types for legacy callers
enum class MenuTargetType {
    PLAYER_EXPANDED,
    QUEUE_ITEM,
    GENERIC_SONG
}

// Data holder for track info
data class MenuSongData(
    val id: String = "",
    val title: String = "",
    val artist: String = "",
    val album: String = "",
    val imageUrl: String = "",
    val durationSeconds: Long = 0L
)

@Stable
class MenuState(
    isVisible: Boolean = false,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    var isVisible by mutableStateOf(isVisible)
    var content by mutableStateOf(content)

    // Legacy target type & song data properties
    var targetType by mutableStateOf(MenuTargetType.PLAYER_EXPANDED)
        private set
    var songData by mutableStateOf<MenuSongData?>(null)
        private set

    fun show(content: @Composable ColumnScope.() -> Unit) {
        isVisible = true
        this.content = content
    }

    fun showPlayerOptions(song: MenuSongData? = null) {
        targetType = MenuTargetType.PLAYER_EXPANDED
        songData = song
        isVisible = true
    }

    fun showQueueItemOptions(song: MenuSongData) {
        targetType = MenuTargetType.QUEUE_ITEM
        songData = song
        isVisible = true
    }

    fun showGenericSongOptions(song: MenuSongData) {
        targetType = MenuTargetType.GENERIC_SONG
        songData = song
        isVisible = true
    }

    fun show() {
        isVisible = true
    }

    fun dismiss() {
        isVisible = false
    }
}

// Composition local holder for menu state
val LocalMenuState = compositionLocalOf { MenuState() }

// Provider layout injecting menu state scope
@Composable
fun ProvideMenuState(content: @Composable () -> Unit) {
    val menuState = remember { MenuState() }
    CompositionLocalProvider(LocalMenuState provides menuState) {
        content()
    }
}
