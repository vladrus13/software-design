package ru.vladrus13.visitors

import ru.vladrus13.Tokenizer
import ru.vladrus13.bean.*
import ru.vladrus13.bean.Number

class Parser(val tokenizer: Tokenizer) {

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

    fun parseMultiplyDivide(): Operator {
        val first = parseMono()
        return when (val operator = tokenizer.get()) {
            is MultiplyOperation, is DivideOperation -> {
                tokenizer.next()
                val second = parseMono()
                BinaryOperation(first, operator as BinaryOperationToken, second)
            }
            else -> first
        }
    }

    private fun parsePlusMinus(): Operator {
        val first = parseMono()
        return when (val operator = tokenizer.get()) {
            is PlusOperation, is MinusOperation -> {
                tokenizer.next()
                val second = parseMono()
                BinaryOperation(first, operator as BinaryOperationToken, second)
            }
            else -> first
        }
    }

    fun parse(): Operator {
        return parsePlusMinus()
    }
}