package ru.vladrus13.visitors

import ru.vladrus13.bean.Operator

/**
 * Print visitor. Calculates polska expression interpretation
 *
 */
class PrintVisitor {
    companion object {
        fun toString(operator: Operator): String {
            return operator.toPolska().joinToString(separator = " ") {
                it.toString()
            }
        }
    }
}