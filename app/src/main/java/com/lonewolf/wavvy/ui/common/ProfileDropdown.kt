package com.lonewolf.wavvy.ui.common

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.lonewolf.wavvy.R
import com.lonewolf.wavvy.ui.theme.Poppins

@Composable
fun ProfileDropdown(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(expanded) {
        if (expanded) isVisible = true
    }

    if (expanded || isVisible) {
        Popup(
            onDismissRequest = { isVisible = false },
            properties = PopupProperties(focusable = true),
            offset = IntOffset(-120, 80)
        ) {
            AnimatedVisibility(
                visible = isVisible,
                // Parâmetros nomeados para evitar erro de tipo
                enter = fadeIn(animationSpec = tween(200)) + scaleIn(
                    initialScale = 0.5f,
                    transformOrigin = TransformOrigin(1f, 0f),
                    animationSpec = tween(250)
                ),
                exit = fadeOut(animationSpec = tween(200)) + scaleOut(
                    targetScale = 0.5f,
                    transformOrigin = TransformOrigin(1f, 0f),
                    animationSpec = tween(250)
                )
            ) {
                DisposableEffect(Unit) {
                    onDispose { if (!isVisible) onDismiss() }
                }

                Surface(
                    modifier = Modifier.width(260.dp).padding(8.dp),
                    color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp).copy(alpha = 0.94f),
                    shape = RoundedCornerShape(24.dp),
                    shadowElevation = 16.dp
                ) {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        HeaderItem()

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        )

                        // Itens do menu via XML
                        ProfileMenuItem(Icons.Default.SwitchAccount, stringResource(R.string.menu_switch_account)) { isVisible = false }
                        ProfileMenuItem(Icons.Default.History, stringResource(R.string.menu_history)) { isVisible = false }
                        ProfileMenuItem(Icons.Default.Settings, stringResource(R.string.menu_settings)) {
                            isVisible = false
                            onNavigateToSettings()
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        )

                        // Sair via XML
                        ProfileMenuItem(
                            icon = Icons.Default.Logout,
                            text = stringResource(R.string.menu_logout),
                            tint = MaterialTheme.colorScheme.error // Mudança feita aqui
                        ) {
                            isVisible = false
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderItem() {
    // Cor adaptável baseada no sistema de cores tertiary (Cyan)
    val accent = MaterialTheme.colorScheme.tertiary

    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.AccountCircle, null, tint = accent, modifier = Modifier.size(40.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                text = stringResource(R.string.menu_your_account),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                fontFamily = Poppins,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.menu_email_placeholder),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = accent.copy(alpha = 0.9f),
                fontFamily = Poppins
            )
        }
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    text: String,
    tint: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = tint.copy(alpha = 0.85f), modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(16.dp))
        Text(
            text = text,
            fontFamily = Poppins,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = tint
        )
    }
}
