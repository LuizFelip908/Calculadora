package com.luizfelipe.calculadora

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Estado exibido na tela:
 * [expression] -> cálculo realizado, mostrado na área superior.
 * [display]    -> número atual / resultado, mostrado em destaque.
 */
data class CalculatorState(
    val expression: String = "",
    val display: String = "0"
)

/**
 * Motor da calculadora. Puro Kotlin (sem Android), o que permite
 * testes unitários diretos da lógica de cálculo.
 */
class CalculatorEngine {

    private var firstOperand: Double? = null
    private var pendingOperator: Char? = null
    private var currentInput: String = ""
    private var lastExpression: String = ""
    private var justEvaluated: Boolean = false
    private var isError: Boolean = false

    var state: CalculatorState = CalculatorState()
        private set

    /** Dígitos 0-9 e o separador decimal ".". */
    fun onDigit(digit: Char) {
        if (isError) clearAll()
        if (justEvaluated) {
            // Começa um cálculo novo após "=".
            clearAll()
        }
        if (digit == '.') {
            if (currentInput.contains('.')) return
            currentInput = if (currentInput.isEmpty()) "0." else "$currentInput."
        } else {
            currentInput = if (currentInput == "0") digit.toString() else currentInput + digit
        }
        refreshState()
    }

    /** Operadores: + - * / */
    fun onOperator(op: Char) {
        if (isError) return
        justEvaluated = false

        if (currentInput.isNotEmpty()) {
            if (firstOperand == null) {
                firstOperand = currentInput.toDouble()
            } else if (pendingOperator != null) {
                // Encadeamento: 2 + 3 + ... resolve o parcial primeiro.
                val result = compute(firstOperand!!, currentInput.toDouble(), pendingOperator!!)
                if (result == null) {
                    setError()
                    return
                }
                firstOperand = result
            }
            currentInput = ""
        } else if (firstOperand == null) {
            // Permite iniciar com o valor exibido (ex.: resultado anterior ou 0).
            firstOperand = state.display.toDoubleOrNull() ?: 0.0
        }

        pendingOperator = op
        refreshState()
    }

    /** Tecla "=" */
    fun onEquals() {
        if (isError) return
        val first = firstOperand ?: return
        val op = pendingOperator ?: return
        val second = if (currentInput.isNotEmpty()) currentInput.toDouble() else first

        val result = compute(first, second, op)
        if (result == null) {
            setError()
            return
        }

        lastExpression = "${format(first)} ${symbol(op)} ${format(second)}"
        firstOperand = result
        pendingOperator = null
        currentInput = ""
        justEvaluated = true

        state = CalculatorState(expression = lastExpression, display = format(result))
    }

    /** Tecla "%": converte o valor atual em porcentagem (divide por 100). */
    fun onPercent() {
        if (isError) return
        val value = currentValue() ?: return
        val percent = value / 100.0
        if (justEvaluated || (currentInput.isEmpty() && pendingOperator == null)) {
            firstOperand = percent
            justEvaluated = false
        } else {
            currentInput = trimNumber(percent)
        }
        refreshState()
    }

    /** Tecla "+/-": inverte o sinal do valor atual. */
    fun onToggleSign() {
        if (isError) return
        if (currentInput.isNotEmpty()) {
            currentInput = if (currentInput.startsWith("-")) {
                currentInput.substring(1)
            } else {
                "-$currentInput"
            }
        } else if (firstOperand != null && pendingOperator == null) {
            firstOperand = -firstOperand!!
            justEvaluated = false
        }
        refreshState()
    }

    /** Tecla "C": limpa tudo. */
    fun clearAll() {
        firstOperand = null
        pendingOperator = null
        currentInput = ""
        lastExpression = ""
        justEvaluated = false
        isError = false
        state = CalculatorState()
    }

    /** Tecla "⌫": apaga o último dígito digitado. */
    fun onBackspace() {
        if (isError) {
            clearAll()
            return
        }
        if (justEvaluated) return
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
            if (currentInput == "-" || currentInput == "0") currentInput = ""
        }
        refreshState()
    }

    // ------------------------------------------------------------------

    private fun currentValue(): Double? = when {
        currentInput.isNotEmpty() -> currentInput.toDoubleOrNull()
        firstOperand != null && pendingOperator == null -> firstOperand
        else -> null
    }

    private fun compute(a: Double, b: Double, op: Char): Double? = when (op) {
        '+' -> a + b
        '-' -> a - b
        '*' -> a * b
        '/' -> if (b == 0.0) null else a / b
        else -> null
    }

    private fun setError() {
        isError = true
        firstOperand = null
        pendingOperator = null
        currentInput = ""
        state = CalculatorState(expression = "", display = ERROR_TEXT)
    }

    private fun refreshState() {
        val expr = buildString {
            firstOperand?.let { append(format(it)) }
            pendingOperator?.let { append(" ${symbol(it)}") }
            if (currentInput.isNotEmpty() && pendingOperator != null) {
                append(" ${formatInput(currentInput)}")
            }
        }

        val display = when {
            currentInput.isNotEmpty() -> formatInput(currentInput)
            pendingOperator != null && firstOperand != null -> format(firstOperand!!)
            firstOperand != null -> format(firstOperand!!)
            else -> "0"
        }

        state = CalculatorState(
            expression = if (justEvaluated) lastExpression else expr,
            display = display
        )
    }

    private fun symbol(op: Char): String = when (op) {
        '/' -> "÷"
        '*' -> "×"
        '-' -> "−"
        else -> "+"
    }

    /** Formata números com separador de milhar, como no design (ex.: 20,810). */
    private fun format(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return ERROR_TEXT
        val symbols = DecimalFormatSymbols(Locale.US)
        val formatter = DecimalFormat("#,##0.########", symbols)
        return formatter.format(value)
    }

    /** Formata o que está sendo digitado sem perder o "." ou zeros finais. */
    private fun formatInput(input: String): String {
        val negative = input.startsWith("-")
        val body = if (negative) input.substring(1) else input
        val intPart = body.substringBefore('.')
        val decPart = if (body.contains('.')) "." + body.substringAfter('.') else ""
        val groupedInt = intPart.toLongOrNull()?.let {
            DecimalFormat("#,##0", DecimalFormatSymbols(Locale.US)).format(it)
        } ?: intPart
        return (if (negative) "-" else "") + groupedInt + decPart
    }

    private fun trimNumber(value: Double): String {
        return if (value % 1.0 == 0.0 && kotlin.math.abs(value) < 1e15) {
            value.toLong().toString()
        } else {
            value.toString()
        }
    }

    companion object {
        const val ERROR_TEXT = "Erro"
    }
}
