package ru.vladrus13

import org.junit.Test
import ru.vladrus13.visitors.CalcVisitor
import ru.vladrus13.visitors.Parser
import ru.vladrus13.visitors.PrintVisitor
import kotlin.test.assertEquals

class ParserTest {

    private fun testAbstract(input : String, polska : String, result : Long) {
        val operator = Parser(Tokenizer(input)).parse()
        assertEquals(polska, PrintVisitor.toString(operator), "Polska view")
        assertEquals(result, CalcVisitor.calc(operator), "Calc operator")
    }

    @Test
    fun testSimple() {
        testAbstract("2", "2", 2)
    }

    @Test
    fun testPlus() {
        testAbstract("3 + 3", "3 3 +", 6)
    }

    @Test
    fun testMinus() {
        testAbstract("3 - 3", "3 3 -", 0)
    }

    @Test
    fun testMultiply() {
        testAbstract("3 * 3", "3 3 *", 9)
    }

    @Test
    fun testDivide() {
        testAbstract("3 / 3", "3 3 /", 1)
    }

    @Test
    fun testBig() {
        testAbstract("(23 + 10) * 5 - 3 * (32 + 5) * (10 - 4 * 5) + 8 / 2", "23 10 + 5 * 3 32 5 + * 10 4 5 * - * - 8 2 / +", 1279)
    }
}