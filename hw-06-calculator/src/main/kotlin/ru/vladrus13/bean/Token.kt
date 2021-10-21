package ru.vladrus13.bean

/**
 * Terminal. Mono point of expression
 *
 */
enum class Terminal {
    PLUS {
        override fun isStarts(c: Char): Boolean = c == '+'

        override fun takeToken(s: String, position: Int): Pair<Int, Token?> =
            if (isStarts(s[position])) Pair(position + 1, PlusOperation()) else Pair(position, null)

    },
    MINUS {
        override fun isStarts(c: Char): Boolean = c == '-'

        override fun takeToken(s: String, position: Int): Pair<Int, Token?> =
            if (isStarts(s[position])) Pair(position + 1, MinusOperation()) else Pair(position, null)
    },
    MULTIPLY {
        override fun isStarts(c: Char): Boolean = c == '*'

        override fun takeToken(s: String, position: Int): Pair<Int, Token?> =
            if (isStarts(s[position])) Pair(position + 1, MultiplyOperation()) else Pair(position, null)
    },
    DIVIDE {
        override fun isStarts(c: Char): Boolean = c == '/'

        override fun takeToken(s: String, position: Int): Pair<Int, Token?> =
            if (isStarts(s[position])) Pair(position + 1, DivideOperation()) else Pair(position, null)
    },
    OPEN_BRACKET {
        override fun isStarts(c: Char): Boolean = c == '('

        override fun takeToken(s: String, position: Int): Pair<Int, Token?> =
            if (isStarts(s[position])) Pair(position + 1, OpenBracket()) else Pair(position, null)
    },
    CLOSE_BRACKET {
        override fun isStarts(c: Char): Boolean = c == ')'

        override fun takeToken(s: String, position: Int): Pair<Int, Token?> =
            if (isStarts(s[position])) Pair(position + 1, CloseBracket()) else Pair(position, null)
    },
    NUMBER {
        override fun isStarts(c: Char): Boolean = Character.isDigit(c)

        override fun takeToken(s: String, position: Int): Pair<Int, Token?> {
            var newPosition = position
            while (newPosition < s.length && Character.isDigit(s[newPosition])) {
                newPosition++
            }
            return Pair(newPosition, NumberToken(s.substring(position, newPosition).toLong()))
        }
    };

    abstract fun isStarts(c: Char): Boolean
    abstract fun takeToken(s: String, position: Int): Pair<Int, Token?>
}

/**
 * Token is like terminal but with data
 *
 */
interface Token {
    override fun toString(): String
}

/**
 * Binary operation
 *
 */
abstract class BinaryOperationToken : Token {
    abstract fun getOperator(): String

    abstract fun eval(a: Long, b: Long): Long

    override fun toString(): String {
        return getOperator()
    }
}

open class PlusOperation : BinaryOperationToken() {
    override fun getOperator(): String = "+"
    override fun eval(a: Long, b: Long): Long = a + b
}

open class MinusOperation : BinaryOperationToken() {
    override fun getOperator(): String = "-"
    override fun eval(a: Long, b: Long): Long = a - b
}

open class MultiplyOperation : BinaryOperationToken() {
    override fun getOperator(): String = "*"
    override fun eval(a: Long, b: Long): Long = a * b
}

open class DivideOperation : BinaryOperationToken() {
    override fun getOperator(): String = "/"
    override fun eval(a: Long, b: Long): Long = a / b
}

abstract class BracketToken : Token {
    abstract fun getBracket(): Char

    override fun toString(): String {
        return "${getBracket()}"
    }
}

open class OpenBracket : BracketToken() {
    override fun getBracket(): Char = '('
}

open class CloseBracket : BracketToken() {
    override fun getBracket(): Char = ')'
}

open class NumberToken(val number: Long) : Token {
    override fun toString(): String {
        return number.toString()
    }
}