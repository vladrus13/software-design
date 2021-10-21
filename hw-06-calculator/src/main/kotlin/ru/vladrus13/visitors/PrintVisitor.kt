package ru.vladrus13.visitors

import ru.vladrus13.bean.Operator

class PrintVisitor {
    companion object {
        fun toString(operator: Operator): String {
            return operator.toPolska().joinToString(separator = " ") {
                it.toString()
            }
        }
    }
}