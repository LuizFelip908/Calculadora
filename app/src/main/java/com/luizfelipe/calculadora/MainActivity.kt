package com.luizfelipe.calculadora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luizfelipe.calculadora.ui.theme.CalculadoraTheme
import com.luizfelipe.calculadora.ui.theme.LocalCalcPalette

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculadoraTheme {
                CalculatorApp()
            }
        }
    }
}

@Composable
fun CalculatorApp(viewModel: CalculatorViewModel = viewModel()) {
    CalculatorScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
fun CalculatorScreen(
    state: CalculatorState,
    onAction: (CalculatorAction) -> Unit
) {
    val palette = LocalCalcPalette.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = palette.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // ---------- Área do visor ----------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.End
            ) {
                // Cálculo realizado (área superior)
                Text(
                    text = state.expression,
                    color = palette.textExpression,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState(), reverseScrolling = true)
                )

                // Resultado em destaque
                Text(
                    text = state.display,
                    color = palette.textPrimary,
                    fontSize = 64.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 24.dp)
                        .horizontalScroll(rememberScrollState(), reverseScrolling = true)
                )
            }

            // ---------- Teclado ----------
            Keyboard(onAction = onAction)
        }
    }
}

@Composable
private fun Keyboard(onAction: (CalculatorAction) -> Unit) {
    val spacing = 12.dp

    Column(verticalArrangement = Arrangement.spacedBy(spacing)) {

        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            CalcKey("C", KeyStyle.FUNCTION, Modifier.weight(1f)) { onAction(CalculatorAction.Clear) }
            CalcKey("+/−", KeyStyle.FUNCTION, Modifier.weight(1f)) { onAction(CalculatorAction.ToggleSign) }
            CalcKey("%", KeyStyle.FUNCTION, Modifier.weight(1f)) { onAction(CalculatorAction.Percent) }
            CalcKey("÷", KeyStyle.OPERATOR, Modifier.weight(1f)) { onAction(CalculatorAction.Operator('/')) }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            CalcKey("7", KeyStyle.NUMBER, Modifier.weight(1f)) { onAction(CalculatorAction.Digit('7')) }
            CalcKey("8", KeyStyle.NUMBER, Modifier.weight(1f)) { onAction(CalculatorAction.Digit('8')) }
            CalcKey("9", KeyStyle.NUMBER, Modifier.weight(1f)) { onAction(CalculatorAction.Digit('9')) }
            CalcKey("×", KeyStyle.OPERATOR, Modifier.weight(1f)) { onAction(CalculatorAction.Operator('*')) }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            CalcKey("4", KeyStyle.NUMBER, Modifier.weight(1f)) { onAction(CalculatorAction.Digit('4')) }
            CalcKey("5", KeyStyle.NUMBER, Modifier.weight(1f)) { onAction(CalculatorAction.Digit('5')) }
            CalcKey("6", KeyStyle.NUMBER, Modifier.weight(1f)) { onAction(CalculatorAction.Digit('6')) }
            CalcKey("−", KeyStyle.OPERATOR, Modifier.weight(1f)) { onAction(CalculatorAction.Operator('-')) }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            CalcKey("1", KeyStyle.NUMBER, Modifier.weight(1f)) { onAction(CalculatorAction.Digit('1')) }
            CalcKey("2", KeyStyle.NUMBER, Modifier.weight(1f)) { onAction(CalculatorAction.Digit('2')) }
            CalcKey("3", KeyStyle.NUMBER, Modifier.weight(1f)) { onAction(CalculatorAction.Digit('3')) }
            CalcKey("+", KeyStyle.OPERATOR, Modifier.weight(1f)) { onAction(CalculatorAction.Operator('+')) }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
            // Tecla "0" larga, como no design de referência
            CalcKey("0", KeyStyle.NUMBER, Modifier.weight(2.17f), wide = true) {
                onAction(CalculatorAction.Digit('0'))
            }
            CalcKey(".", KeyStyle.NUMBER, Modifier.weight(1f)) { onAction(CalculatorAction.Digit('.')) }
            CalcKey("=", KeyStyle.OPERATOR, Modifier.weight(1f)) { onAction(CalculatorAction.Equals) }
        }
    }
}

private enum class KeyStyle { NUMBER, FUNCTION, OPERATOR }

@Composable
private fun CalcKey(
    label: String,
    style: KeyStyle,
    modifier: Modifier = Modifier,
    wide: Boolean = false,
    onClick: () -> Unit
) {
    val palette = LocalCalcPalette.current

    val background = when (style) {
        KeyStyle.NUMBER -> palette.keyNumber
        KeyStyle.FUNCTION -> palette.keyFunction
        KeyStyle.OPERATOR -> palette.keyOperator
    }
    val contentColor = when (style) {
        KeyStyle.NUMBER -> palette.textPrimary
        KeyStyle.FUNCTION -> palette.textOnFunction
        KeyStyle.OPERATOR -> palette.textOnOperator
    }

    val shape = RoundedCornerShape(26.dp)

    Box(
        modifier = modifier
            .aspectRatio(if (wide) 2.17f else 1f)
            .shadow(6.dp, shape, spotColor = Color.Black.copy(alpha = 0.35f))
            .clip(shape)
            .background(background)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = contentColor,
            fontSize = if (style == KeyStyle.OPERATOR) 32.sp else 26.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CalculatorPreviewDark() {
    CalculadoraTheme(darkTheme = true) {
        CalculatorScreen(
            state = CalculatorState(expression = "4,900 + 15,910", display = "20,810"),
            onAction = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CalculatorPreviewLight() {
    CalculadoraTheme(darkTheme = false) {
        CalculatorScreen(
            state = CalculatorState(expression = "30,820 + 9,205", display = "40,025"),
            onAction = {}
        )
    }
}
