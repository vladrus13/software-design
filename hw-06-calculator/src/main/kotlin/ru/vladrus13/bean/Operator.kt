package ru.vladrus13.bean

import java.util.*

abstract class Operator {
    abstract fun eval(): Long
    abstract fun toPolska(): LinkedList<Token>
}

class Number(private val number: NumberToken) : Operator() {
    override fun eval(): Long = number.number

    override fun toPolska(): LinkedList<Token> = LinkedList<Token>().apply {
        add(number)
    }
}

class BinaryOperation(
    private val left: Operator,
    private val operation: BinaryOperationToken,
    private val right: Operator
) : Operator() {
    override fun eval(): Long = operation.eval(left.eval(), right.eval())

    override fun toPolska(): LinkedList<Token> = LinkedList<Token>().apply {
        addAll(left.toPolska())
        addAll(right.toPolska())
        add(operation)
    }
}