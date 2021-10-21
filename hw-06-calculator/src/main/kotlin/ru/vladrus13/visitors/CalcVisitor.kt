package ru.vladrus13.visitors

import ru.vladrus13.bean.BinaryOperationToken
import ru.vladrus13.bean.NumberToken
import ru.vladrus13.bean.Operator
import java.util.*

/**
 * Calculator visitor. Calculates result of the expression
 *
 */
class CalcVisitor {
    companion object {
        fun calc(operator: Operator) : Long {
            val result = LinkedList<Long>()
            operator.toPolska().forEach {
                when (it) {
                    is NumberToken -> {
                        result.add(it.number)
                    }
                    is BinaryOperationToken -> {
                        if (result.size < 2) {
                            throw IllegalStateException("Can't calculate less than two numbers")
                        }
                        val second = result.removeLast()
                        val first = result.removeLast()
                        result.addLast(it.eval(first, second))
                    }
                }
            }
            if (result.size != 1) {
                throw IllegalStateException("Can't be on result more than 2 numbers")
            }
            return result.first
        }
    }
}