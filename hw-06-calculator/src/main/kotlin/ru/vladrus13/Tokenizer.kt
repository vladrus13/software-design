package ru.vladrus13

import ru.vladrus13.bean.Terminal
import ru.vladrus13.bean.Token
import java.text.ParseException

class Tokenizer(private val s: String) {

    private var token: Token? = null
    private var currentPosition = 0

    fun get(): Token? = token

    private fun skipSpaces() {
        while (s.length > currentPosition && Character.isWhitespace(s[currentPosition])) {
            currentPosition++
        }
    }

    fun next() {
        skipSpaces()
        if (s.length == currentPosition) {
            return
        }
        for (terminal in Terminal.values()) {
            if (terminal.isStarts(s[currentPosition])) {
                val result = terminal.takeToken(s, currentPosition)
                currentPosition = result.first
                if (result.second == null) {
                    throw ParseException("Found correctless subsequence", currentPosition - 1)
                }
                token = result.second!!
                return
            }
        }
        throw ParseException("Can't find token to this character: ${s[currentPosition]}", currentPosition)
    }
}