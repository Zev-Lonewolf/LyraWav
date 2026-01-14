package com.lonewolf.lyrawav.ui.home

import java.util.Calendar
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lonewolf.lyrawav.ui.theme.Poppins

// Lógica de saudação
fun getWelcomeMessage(userName: String?): String {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)

    val greeting = when (hour) {
        in 5..11 -> "Bom dia"
        in 12..17 -> "Boa tarde"
        else -> "Boa noite"
    }
    return if (userName.isNullOrBlank()) "$greeting!" else "$greeting, $userName!"
}

@Composable
fun GreetingSection(userName: String?) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)

    val greeting = when (hour) {
        in 5..11 -> "Bom dia"
        in 12..17 -> "Boa tarde"
        else -> "Boa noite"
    }

    // Seção de boas-vindas
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Título principal
        Text(
            text = androidx.compose.ui.text.buildAnnotatedString {
                append("$greeting")
                if (!userName.isNullOrBlank()) {
                    append(", ")
                    withStyle(style = androidx.compose.ui.text.SpanStyle(
                        color = Color(0xFF00E5FF),
                        fontWeight = FontWeight.ExtraBold
                    )) {
                        append(userName)
                    }
                }
                append("!")
            },
            style = TextStyle(
                fontFamily = Poppins,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        // Subtítulo
        Text(
            text = "Qual o estilo musical de hoje?",
            style = TextStyle(
                fontFamily = Poppins,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            ),
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
