package com.lonewolf.wavvy.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lonewolf.wavvy.R
import com.lonewolf.wavvy.ui.common.ProfileDropdown
import com.lonewolf.wavvy.ui.theme.Poppins

@Composable
fun HomeHeader(onNavigateToSettings: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    val cyan = MaterialTheme.colorScheme.tertiary
    val outline = MaterialTheme.colorScheme.onBackground

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = buildAnnotatedString {
                // Contorno
                withStyle(
                    SpanStyle(
                        color = cyan,
                        fontWeight = FontWeight.Black,
                        shadow = Shadow(
                            color = cyan.copy(alpha = 0.45f),
                            blurRadius = 20f
                        )
                    )
                ) {
                    append(stringResource(R.string.logo_Home))
                }
            },
            fontFamily = Poppins,
            fontSize = 28.sp
        )

        Box {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                        CircleShape
                    )
                    .clip(CircleShape)
                    .clickable { expanded = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(R.string.cd_profile_button),
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(20.dp)
                )
            }

            ProfileDropdown(
                expanded = expanded,
                onDismiss = { expanded = false },
                onNavigateToSettings = onNavigateToSettings
            )
        }
    }
}
