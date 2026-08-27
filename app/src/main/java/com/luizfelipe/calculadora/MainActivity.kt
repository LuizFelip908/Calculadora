package com.luizfelipe.calculadora

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.luizfelipe.calculadora.databinding.ActivityMainBinding
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    /** Tokens já confirmados da expressão: número, operador, número... */
    private val tokens = mutableListOf<String>()

    /** Número que está sendo digitado no momento (usa '.' internamente). */
    private var currentInput = ""

    /** Indica que o último toque foi em "=" (próximo dígito começa conta nova). */
    private var justEvaluated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupNumberButtons()
        setupOperatorButtons()
        setupFunctionButtons()
        updateDisplay()
    }

    private fun setupNumberButtons() {
        val numberButtons = mapOf(
            binding.btn0 to "0", binding.btn1 to "1", binding.btn2 to "2",
            binding.btn3 to "3", binding.btn4 to "4", binding.btn5 to "5",
            binding.btn6 to "6", binding.btn7 to "7", binding.btn8 to "8",
            binding.btn9 to "9"
        )
        numberButtons.forEach { (button, digit) ->
            button.setOnClickListener { onDigit(digit) }
        }
        binding.btnDot.setOnClickListener { onDecimalSeparator() }
    }

    private fun setupOperatorButtons() {
        binding.btnAdd.setOnClickListener { onOperator("+") }
        binding.btnSubtract.setOnClickListener { onOperator("−") }
        binding.btnMultiply.setOnClickListener { onOperator("×") }
        binding.btnDivide.setOnClickListener { onOperator("÷") }
        binding.btnEquals.setOnClickListener { onEquals() }
    }

    private fun setupFunctionButtons() {
        binding.btnClear.setOnClickListener { onClear() }
        binding.btnSign.setOnClickListener { onToggleSign() }
        binding.btnPercent.setOnClickListener { onPercent() }
        binding.btnBackspace.setOnClickListener { onBackspace() }
    }

    // ---------- Entrada de números ----------

    private fun onDigit(digit: String) {
        if (justEvaluated) {
            startNewCalculation()
        }
        currentInput = when {
            currentInput == "0" -> digit
            currentInput == "-0" -> "-$digit"
            else -> currentInput + digit
        }
        updateDisplay()
    }

    private fun onDecimalSeparator() {
        if (justEvaluated) {
            startNewCalculation()
        }
        if (currentInput.contains(".")) return
        currentInput = if (currentInput.isEmpty() || currentInput == "-") {
            currentInput + "0."
        } else {
            "$currentInput."
        }
        updateDisplay()
    }

    // ---------- Operadores ----------

    private fun onOperator(operator: String) {
        if (justEvaluated) {
            // Continua a conta a partir do resultado anterior
            justEvaluated = false
        }
        when {
            currentInput.isNotEmpty() && currentInput != "-" -> {
                tokens.add(normalized(currentInput))
                tokens.add(operator)
                currentInput = ""
            }
            tokens.isNotEmpty() && isOperator(tokens.last()) -> {
                // Troca o operador se o usuário mudar de ideia
                tokens[tokens.size - 1] = operator
            }
            tokens.isEmpty() && operator == "−" -> {
                // Permite começar um número negativo
                currentInput = "-"
            }
            tokens.isEmpty() -> {
                tokens.add("0")
                tokens.add(operator)
            }
        }
        updateDisplay()
    }

    private fun onEquals() {
        if (justEvaluated) return
        if (currentInput.isEmpty() || currentInput == "-") {
            if (tokens.isEmpty()) return
            // Remove operador pendurado no final: "5 +" vira "5"
            if (isOperator(tokens.last())) tokens.removeAt(tokens.size - 1)
        } else {
            tokens.add(normalized(currentInput))
        }
        if (tokens.isEmpty()) return

        try {
            val result = CalculatorEngine.evaluate(tokens)
            binding.tvExpression.text = buildString {
                append(tokens.joinToString(" ") { prettify(it) })
                append(" =")
            }
            currentInput = result.toPlainString()
            binding.tvResult.text = CalculatorEngine.format(result)
            tokens.clear()
            justEvaluated = true
        } catch (e: ArithmeticException) {
            binding.tvExpression.text = tokens.joinToString(" ") { prettify(it) }
            binding.tvResult.text = getString(R.string.error_message)
            tokens.clear()
            currentInput = ""
            justEvaluated = true
        }
    }

    // ---------- Funções especiais ----------

    private fun onClear() {
        tokens.clear()
        currentInput = ""
        justEvaluated = false
        updateDisplay()
    }

    private fun onBackspace() {
        if (justEvaluated) {
            onClear()
            return
        }
        if (currentInput.isNotEmpty()) {
            currentInput = currentInput.dropLast(1)
        } else if (tokens.isNotEmpty()) {
            val last = tokens.removeAt(tokens.size - 1)
            if (!isOperator(last)) currentInput = last
        }
        updateDisplay()
    }

    private fun onToggleSign() {
        if (justEvaluated) justEvaluated = false
        if (currentInput.isEmpty() || currentInput == "-") return
        currentInput = if (currentInput.startsWith("-")) {
            currentInput.substring(1)
        } else {
            "-$currentInput"
        }
        updateDisplay()
    }

    private fun onPercent() {
        if (justEvaluated) justEvaluated = false
        if (currentInput.isEmpty() || currentInput == "-") return
        val value = BigDecimal(normalized(currentInput))

        // Se houver uma soma ou subtração pendente, o % é relativo ao valor
        // anterior (ex.: 200 + 10% = 200 + 20). Caso contrário divide por 100.
        val percent = if (tokens.size >= 2 && (tokens.last() == "+" || tokens.last() == "−")) {
            val base = BigDecimal(tokens[tokens.size - 2])
            base.multiply(value).divide(BigDecimal(100), MathContext(16, RoundingMode.HALF_UP))
        } else {
            value.divide(BigDecimal(100), MathContext(16, RoundingMode.HALF_UP))
        }
        currentInput = percent.stripTrailingZeros().toPlainString()
        updateDisplay()
    }

    // ---------- Exibição ----------

    private fun updateDisplay() {
        val expression = buildString {
            append(tokens.joinToString(" ") { prettify(it) })
            if (currentInput.isNotEmpty()) {
                if (isNotEmpty()) append(" ")
                append(prettify(currentInput))
            }
        }
        binding.tvExpression.text = expression

        binding.tvResult.text = when {
            currentInput.isNotEmpty() && currentInput != "-" ->
                prettify(currentInput)
            tokens.isNotEmpty() ->
                prettify(tokens.first { !isOperator(it) })
            else -> "0"
        }
    }

    private fun startNewCalculation() {
        tokens.clear()
        currentInput = ""
        justEvaluated = false
    }

    private fun isOperator(token: String) =
        token == "+" || token == "−" || token == "×" || token == "÷"

    /** Garante um número válido para o BigDecimal ("5." -> "5"). */
    private fun normalized(input: String): String {
        var value = input
        if (value.endsWith(".")) value = value.dropLast(1)
        if (value.isEmpty() || value == "-") value = "0"
        return value
    }

    /** Converte a notação interna para exibição (ponto -> vírgula). */
    private fun prettify(token: String): String =
        if (isOperator(token)) token else token.replace('.', ',')
}
