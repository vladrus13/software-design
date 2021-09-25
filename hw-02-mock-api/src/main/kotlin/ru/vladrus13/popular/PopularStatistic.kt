package ru.vladrus13.popular

import java.util.*

interface PopularStatistic {
    fun get(code: String, hours: Int, currentTime : Long = Date().time): List<Int>
}