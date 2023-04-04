package com.elvinliang.aviation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

private val LightColors = lightColorScheme(
    primary = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    onPrimaryContainer = Color(0xB2646464),
    secondary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onSecondaryContainer = Color(0xFF363636),
    tertiary = Color(0xFF000000),
    onTertiary = Color(0xFF7A7A7A),
    onTertiaryContainer = Color(0xFFC6C6C6),
// ..
)

val ColorScheme.Button: ButtonColors
    @Composable
    get() = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF6750A4),
        contentColor = Color(0xFF6750A4),
        disabledContainerColor = Color(0x7B6750A4),
        disabledContentColor = Color(0xFF6750A4)
    )

val ColorScheme.onButton: Color @Composable
get() = if (!isSystemInDarkTheme()) Color(0xFF000000) else Color(0xFFFFFFFF)

val ColorScheme.onButtonContainer: Color @Composable
get() = if (!isSystemInDarkTheme()) Color(0xFF6750A4) else Color(0xFF6750A4)

val ColorScheme.button: Color @Composable
get() = if (!isSystemInDarkTheme()) Color(0xFFFFFFFF) else Color(0xFFFFFFFF)

val ColorScheme.text: Color @Composable
get() = if (!isSystemInDarkTheme()) Color(0xFF000000) else Color(0xFFFFFFFF)

val ColorScheme.onCardContainer: Color @Composable
get() = if (!isSystemInDarkTheme()) Color(0xFFFF9800) else Color(0xFFFF9800)

val ColorScheme.onCard: Color @Composable
get() = if (!isSystemInDarkTheme()) Color(0xFF000000) else Color(0xFF000000)

val ColorScheme.card: Color @Composable
get() = if (!isSystemInDarkTheme()) Color(0xFF000000) else Color(0xFF000000)

@Composable
fun LoginPageTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
//    val colors = if (darkTheme) {
//        DarkColorPalette
//    } else {
//        LightColorPalette
//    }
    val colors = LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}

@Preview
@Composable
fun testPageTheme() {
    LoginPageTheme {
        Row(
            Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "testPageTheme ${MaterialTheme.colorScheme.primary}")
        }
    }
}
