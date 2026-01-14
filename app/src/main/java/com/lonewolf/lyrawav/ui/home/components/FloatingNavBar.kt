package com.lonewolf.lyrawav.ui.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lonewolf.lyrawav.ui.theme.Poppins

@Composable
fun FloatingNavBar() {
    // Barra de navegação flutuante
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        // Container da barra
        Row(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(58.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                .border(
                    width = 0.5.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(50.dp)
                )
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavIcon(Icons.Default.Home, "Início", isSelected = true)
            NavIcon(Icons.Default.Search, "Explorar", isSelected = false)
            NavIcon(Icons.AutoMirrored.Filled.List, "Biblioteca", isSelected = false)
        }
    }
}

@Composable
fun NavIcon(icon: ImageVector, label: String, isSelected: Boolean) {
    // Ícone individual
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { /* Navegação */ }
            .padding(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Color(0xFF00BFA5) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 9.sp,
            color = if (isSelected) Color(0xFF00BFA5) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
