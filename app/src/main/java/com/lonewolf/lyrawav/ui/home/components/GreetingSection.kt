package com.lonewolf.lyrawav.ui.home.components

import java.util.Calendar
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lonewolf.lyrawav.R
import com.lonewolf.lyrawav.ui.theme.Poppins

@Composable
fun GreetingSection(userName: String?) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)

    // Busca a saudação traduzida
    val greeting = when (hour) {
        in 5..11 -> stringResource(R.string.greeting_morning)
        in 12..17 -> stringResource(R.string.greeting_afternoon)
        else -> stringResource(R.string.greeting_evening)
    }

    // Seção de boas-vindas
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Título principal com nome destacado
        Text(
            text = buildAnnotatedString {
                append(greeting)
                if (!userName.isNullOrBlank()) {
                    append(", ")
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF00E5FF),
                            fontWeight = FontWeight.ExtraBold
                        )
                    ) {
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
            text = stringResource(R.string.greeting_question),
            style = TextStyle(
                fontFamily = Poppins,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            ),
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
