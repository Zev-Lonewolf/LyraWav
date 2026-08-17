package com.wavvy.app.core.designsystem.bottomsheet

// Android and Compose layout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
// Material 3 components
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetState
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
// Compose runtime & UI
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Animated bottom sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedBottomSheet(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false),
    sheetMaxWidth: Dp = BottomSheetDefaults.SheetMaxWidth,
    shape: Shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    tonalElevation: Dp = 0.dp,
    scrimColor: Color = BottomSheetDefaults.ScrimColor,
    dragHandle: @Composable (() -> Unit)? = {
        Box(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .size(width = 40.dp, height = 4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
        )
    },
    contentWindowInsets: @Composable () -> WindowInsets = { BottomSheetDefaults.windowInsets },
    properties: ModalBottomSheetProperties = ModalBottomSheetProperties(shouldDismissOnBackPress = true),
    content: @Composable ColumnScope.() -> Unit,
) {
    var lastContent by remember { mutableStateOf(content) }

    LaunchedEffect(content) {
        if (isVisible) lastContent = content
    }

    LaunchedEffect(isVisible) {
        if (isVisible) sheetState.show() else sheetState.hide()
    }

    if (!sheetState.isVisible && !isVisible) return

    val isTransitioning by remember(sheetState) {
        derivedStateOf { sheetState.targetValue != sheetState.currentValue }
    }

    ModalBottomSheet(
        onDismissRequest = {
            if (!isTransitioning) onDismissRequest()
        },
        modifier = modifier,
        sheetState = sheetState,
        sheetMaxWidth = sheetMaxWidth,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tonalElevation = tonalElevation,
        scrimColor = scrimColor,
        dragHandle = {
            Box {
                dragHandle?.invoke()
                if (isTransitioning) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                                        event.changes.forEach { it.consume() }
                                    }
                                }
                            }
                    )
                }
            }
        },
        contentWindowInsets = contentWindowInsets,
        properties = properties,
        content = {
            Box(modifier = Modifier.fillMaxWidth()) {
                val columnScope = this@ModalBottomSheet
                columnScope.lastContent()

                if (isTransitioning) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent(pass = PointerEventPass.Initial)
                                        event.changes.forEach { it.consume() }
                                    }
                                }
                            }
                    )
                }
            }
        },
    )
}

// Bottom sheet menu
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetMenu(
    modifier: Modifier = Modifier,
    state: MenuState,
    background: Color = MaterialTheme.colorScheme.background,
) {
    val focusManager = LocalFocusManager.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    AnimatedBottomSheet(
        isVisible = state.isVisible,
        onDismissRequest = {
            focusManager.clearFocus()
            state.isVisible = false
        },
        sheetState = sheetState,
        containerColor = background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .size(width = 40.dp, height = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
            )
        },
        modifier = modifier.fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            state.content(this)
        }
    }
}

// Sequential bottom sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SequentialBottomSheet(
    onDismiss: () -> Unit,
    autoWrap: Boolean = false,
    startExpanded: Boolean = false,
    content: @Composable ColumnScope.(animateDismiss: () -> Unit) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = autoWrap || startExpanded
    )

    AnimatedBottomSheet(
        isVisible = true,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            content(onDismiss)
        }
    }
}
