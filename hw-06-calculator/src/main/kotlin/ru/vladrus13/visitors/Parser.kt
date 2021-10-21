package ru.vladrus13.visitors

import ru.vladrus13.Tokenizer
import ru.vladrus13.bean.*
import ru.vladrus13.bean.Number

/**
 * Parser, which task is parsing tokenizer token stream
 *
 * @property tokenizer
 */
class Parser(private val tokenizer: Tokenizer) {

    private fun parseMono(): Operator {
        val token = tokenizer.get() ?: throw IllegalStateException("Missing mono")
        return when (token) {
            is NumberToken -> {
                tokenizer.next()
                Number(token)
            }
            is OpenBracket -> {
                tokenizer.next()
                val under = parsePlusMinus()
                val close = tokenizer.get()
                tokenizer.next()
                if (close !is CloseBracket) {
                    throw IllegalStateException("Must be close bracket")
                } else {
                    under
                }
            }
            else -> throw IllegalStateException("Unexpected token: $token")
        }
    }

    private fun parseMultiplyDivide(): Operator {
        var result = parseMono()
        var operator = tokenizer.get()
        while (true) {
            when (operator) {
                is MultiplyOperation, is DivideOperation -> {
                    tokenizer.next()
                    val second = parseMono()
                    result = BinaryOperation(result, operator as BinaryOperationToken, second)
                    operator = tokenizer.get()
                }
                else -> break
            }
        }
        return result
    }

    private fun parsePlusMinus(): Operator {
        var result = parseMultiplyDivide()
        var operator = tokenizer.get()
        while (true) {
            when (operator) {
                is PlusOperation, is MinusOperation -> {
                    tokenizer.next()
                    val second = parseMultiplyDivide()
                    result = BinaryOperation(result, operator as BinaryOperationToken, second)
                    operator = tokenizer.get()
                }
                else -> break
            }
        }
        return result
    }

    fun parse(): Operator {
        tokenizer.next()
        return parsePlusMinus()
    }
}