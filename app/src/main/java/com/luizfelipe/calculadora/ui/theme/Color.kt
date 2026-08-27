package com.luizfelipe.calculadora.ui.theme

import androidx.compose.ui.graphics.Color

/** Paleta inspirada no design de referência (versões dark e light). */
object CalcColors {

    // Laranja dos operadores (igual nos dois temas)
    val Orange = Color(0xFFF7941D)
    val OrangeDark = Color(0xFFE07E00)

    // ----- Tema escuro -----
    val DarkBackground = Color(0xFF17181D)
    val DarkKeyNumber = Color(0xFF2A2C34)
    val DarkKeyFunction = Color(0xFF4E5462)
    val DarkTextPrimary = Color(0xFFF5F5F7)
    val DarkTextExpression = Color(0xFF8A8D99)

    // ----- Tema claro -----
    val LightBackground = Color(0xFFEDEFF4)
    val LightKeyNumber = Color(0xFFFDFDFF)
    val LightKeyFunction = Color(0xFFD5DAE5)
    val LightTextPrimary = Color(0xFF2E3138)
    val LightTextExpression = Color(0xFF9AA0AE)
}

/** Conjunto de cores usado pela tela conforme o tema do sistema. */
data class CalcPalette(
    val background: Color,
    val keyNumber: Color,
    val keyFunction: Color,
    val keyOperator: Color,
    val textPrimary: Color,
    val textExpression: Color,
    val textOnOperator: Color,
    val textOnFunction: Color
)

val DarkPalette = CalcPalette(
    background = CalcColors.DarkBackground,
    keyNumber = CalcColors.DarkKeyNumber,
    keyFunction = CalcColors.DarkKeyFunction,
    keyOperator = CalcColors.Orange,
    textPrimary = CalcColors.DarkTextPrimary,
    textExpression = CalcColors.DarkTextExpression,
    textOnOperator = Color.White,
    textOnFunction = Color.White
)

val LightPalette = CalcPalette(
    background = CalcColors.LightBackground,
    keyNumber = CalcColors.LightKeyNumber,
    keyFunction = CalcColors.LightKeyFunction,
    keyOperator = CalcColors.Orange,
    textPrimary = CalcColors.LightTextPrimary,
    textExpression = CalcColors.LightTextExpression,
    textOnOperator = Color.White,
    textOnFunction = CalcColors.LightTextPrimary
)
