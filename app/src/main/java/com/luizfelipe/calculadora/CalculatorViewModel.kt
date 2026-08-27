package com.luizfelipe.calculadora

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/** Ações possíveis do teclado da calculadora. */
sealed interface CalculatorAction {
    data class Digit(val value: Char) : CalculatorAction
    data class Operator(val op: Char) : CalculatorAction
    data object Equals : CalculatorAction
    data object Percent : CalculatorAction
    data object ToggleSign : CalculatorAction
    data object Clear : CalculatorAction
    data object Backspace : CalculatorAction
}

class CalculatorViewModel : ViewModel() {

    private val engine = CalculatorEngine()

    var state by mutableStateOf(engine.state)
        private set

    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Digit -> engine.onDigit(action.value)
            is CalculatorAction.Operator -> engine.onOperator(action.op)
            CalculatorAction.Equals -> engine.onEquals()
            CalculatorAction.Percent -> engine.onPercent()
            CalculatorAction.ToggleSign -> engine.onToggleSign()
            CalculatorAction.Clear -> engine.clearAll()
            CalculatorAction.Backspace -> engine.onBackspace()
        }
        state = engine.state
    }
}
