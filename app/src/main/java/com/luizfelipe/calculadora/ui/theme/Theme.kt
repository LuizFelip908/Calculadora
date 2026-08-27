package com.luizfelipe.calculadora.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalCalcPalette = staticCompositionLocalOf { DarkPalette }

@Composable
fun CalculadoraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val palette = if (darkTheme) DarkPalette else LightPalette

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = CalcColors.Orange,
            background = palette.background,
            surface = palette.background
        )
    } else {
        lightColorScheme(
            primary = CalcColors.Orange,
            background = palette.background,
            surface = palette.background
        )
    }

    androidx.compose.runtime.CompositionLocalProvider(LocalCalcPalette provides palette) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}
