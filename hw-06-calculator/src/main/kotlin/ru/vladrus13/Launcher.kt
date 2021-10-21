package ru.vladrus13

import ru.vladrus13.visitors.CalcVisitor
import ru.vladrus13.visitors.Parser
import ru.vladrus13.visitors.PrintVisitor

fun main() {
    val list = listOf(
        "2",
        "2 + 2",
        "2 + 2 * 2",
        "2 * 2 + 2",
        "(23 + 10) * 5 - 3 * (32 + 5) * (10 - 4 * 5) + 8 / 2"
    )
    for (element in list) {
        val operator = Parser(Tokenizer(element)).parse()
        println("Start: ${element}, polska: ${PrintVisitor.toString(operator)}, result: ${CalcVisitor.calc(operator)}")
    }
}