package com.elvinliang.aviation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.elvinliang.aviation.R

private val DarkColorPalette = darkColors(
    primary = Color(R.color.light_gray),
    primaryVariant = Color(R.color.orange),
    secondary = Color(R.color.white)
)

private val LightColorPalette = lightColors(
    primary = Color(R.color.light_gray),
    primaryVariant = Color(R.color.orange),
    secondary = Color(R.color.white)
)

@Composable
fun LoginPageTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}