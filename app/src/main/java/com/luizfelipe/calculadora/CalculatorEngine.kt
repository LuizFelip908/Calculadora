package com.luizfelipe.calculadora

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

/**
 * Motor de cálculo da calculadora.
 *
 * Avalia expressões com soma, subtração, multiplicação e divisão,
 * respeitando a precedência dos operadores (× e ÷ antes de + e −).
 */
object CalculatorEngine {

    class DivisionByZeroException : ArithmeticException("Divisão por zero")

    /**
     * Avalia uma lista de tokens no formato:
     * [numero, operador, numero, operador, numero, ...]
     *
     * Exemplo: ["2", "+", "3", "×", "4"] -> 14
     */
    fun evaluate(tokens: List<String>): BigDecimal {
        if (tokens.isEmpty()) return BigDecimal.ZERO

        // 1ª passada: resolve multiplicações e divisões
        val firstPass = ArrayList<String>()
        var i = 0
        while (i < tokens.size) {
            val token = tokens[i]
            if (token == "×" || token == "÷") {
                val left = BigDecimal(firstPass.removeAt(firstPass.size - 1))
                val right = BigDecimal(tokens[i + 1])
                val partial = when (token) {
                    "×" -> left.multiply(right)
                    else -> {
                        if (right.compareTo(BigDecimal.ZERO) == 0) throw DivisionByZeroException()
                        left.divide(right, MathContext(16, RoundingMode.HALF_UP))
                    }
                }
                firstPass.add(partial.toPlainString())
                i += 2
            } else {
                firstPass.add(token)
                i++
            }
        }

        // 2ª passada: resolve somas e subtrações
        var result = BigDecimal(firstPass[0])
        var j = 1
        while (j < firstPass.size - 1) {
            val op = firstPass[j]
            val value = BigDecimal(firstPass[j + 1])
            result = when (op) {
                "+" -> result.add(value)
                "−" -> result.subtract(value)
                else -> result
            }
            j += 2
        }
        return result.stripTrailingZeros()
    }

    /** Formata um número para exibição, removendo zeros desnecessários. */
    fun format(value: BigDecimal): String {
        val stripped = value.stripTrailingZeros()
        val plain = if (stripped.scale() < 0) {
            stripped.setScale(0).toPlainString()
        } else {
            stripped.toPlainString()
        }
        return plain.replace('.', ',')
    }
}
