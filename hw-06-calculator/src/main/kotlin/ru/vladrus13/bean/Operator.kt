package ru.vladrus13.bean

import java.util.*

/**
 * Operator class
 *
 */
abstract class Operator {
    /**
     * Evaluate operation under
     *
     * @return
     */
    abstract fun eval(): Long

    /**
     * Get polska interpretation
     *
     * @return list of polska interpretation
     */
    abstract fun toPolska(): LinkedList<Token>
}

/**
 * Number class
 *
 * @property number number contain
 */
class Number(private val number: NumberToken) : Operator() {
    override fun eval(): Long = number.number

    override fun toPolska(): LinkedList<Token> = LinkedList<Token>().apply {
        add(number)
    }
}

/**
 * Binary operation
 *
 * @property left left operand
 * @property operation operation on binary operation
 * @property right right operand
 */
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