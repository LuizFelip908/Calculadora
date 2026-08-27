package com.luizfelipe.calculadora

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorEngineTest {

    private fun CalculatorEngine.type(sequence: String) {
        sequence.forEach { c ->
            when (c) {
                in '0'..'9', '.' -> onDigit(c)
                '+', '-', '*', '/' -> onOperator(c)
                '=' -> onEquals()
                '%' -> onPercent()
                '~' -> onToggleSign()
                'C' -> clearAll()
                '<' -> onBackspace()
            }
        }
    }

    @Test
    fun soma() {
        val e = CalculatorEngine()
        e.type("4900+15910=")
        assertEquals("20,810", e.state.display)
        assertEquals("4,900 + 15,910", e.state.expression)
    }

    @Test
    fun subtracao() {
        val e = CalculatorEngine()
        e.type("100-42=")
        assertEquals("58", e.state.display)
    }

    @Test
    fun multiplicacao() {
        val e = CalculatorEngine()
        e.type("12*12=")
        assertEquals("144", e.state.display)
    }

    @Test
    fun divisao() {
        val e = CalculatorEngine()
        e.type("144/12=")
        assertEquals("12", e.state.display)
    }

    @Test
    fun divisaoDecimal() {
        val e = CalculatorEngine()
        e.type("1/2=")
        assertEquals("0.5", e.state.display)
    }

    @Test
    fun divisaoPorZeroMostraErro() {
        val e = CalculatorEngine()
        e.type("5/0=")
        assertEquals(CalculatorEngine.ERROR_TEXT, e.state.display)
    }

    @Test
    fun porcentagem() {
        val e = CalculatorEngine()
        e.type("50%")
        assertEquals("0.5", e.state.display)
    }

    @Test
    fun porcentagemDentroDeConta() {
        val e = CalculatorEngine()
        e.type("200*10%=")
        assertEquals("20", e.state.display)
    }

    @Test
    fun alteracaoDeSinal() {
        val e = CalculatorEngine()
        e.type("25~")
        assertEquals("-25", e.state.display)
        e.type("~")
        assertEquals("25", e.state.display)
    }

    @Test
    fun encadeamentoDeOperacoes() {
        val e = CalculatorEngine()
        e.type("2+3+4=")
        assertEquals("9", e.state.display)
    }

    @Test
    fun expressaoApareceNaAreaSuperior() {
        val e = CalculatorEngine()
        e.type("30820+9205")
        assertEquals("30,820 + 9,205", e.state.expression)
        e.type("=")
        assertEquals("40,025", e.state.display)
        assertEquals("30,820 + 9,205", e.state.expression)
    }

    @Test
    fun limpar() {
        val e = CalculatorEngine()
        e.type("123+45C")
        assertEquals("0", e.state.display)
        assertEquals("", e.state.expression)
    }

    @Test
    fun apagarUltimoDigito() {
        val e = CalculatorEngine()
        e.type("123<")
        assertEquals("12", e.state.display)
    }

    @Test
    fun novoNumeroAposIgualComecaCalculoNovo() {
        val e = CalculatorEngine()
        e.type("2+2=5")
        assertEquals("5", e.state.display)
        assertEquals("", e.state.expression)
    }

    @Test
    fun decimalComVirgula() {
        val e = CalculatorEngine()
        e.type("1.5+1.5=")
        assertEquals("3", e.state.display)
    }
}
