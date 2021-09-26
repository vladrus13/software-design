package ru.vladrus13.popular

import java.util.*

/**
 * Class for creating statistics of some API's
 *
 */
abstract class PopularStatisticSize {
    /**
     * Get statistics in one hour
     *
     * @param code substring of posts
     * @param time numbers of hours we get
     * @return list of posts
     */
    protected abstract fun get(code: String, time: Pair<Long, Long>): List<String>

    /**
     * Get statistics hour by hour on API with substring
     *
     * @param code substring of posts
     * @param hours numbers of hours we get
     * @param currentTime current time. We can get another time
     * @return list of count posts
     */
    fun get(code: String, hours: Int, currentTime: Long = Date().time): List<Int> {
        if (hours <= 0) return emptyList()
        val epoch = currentTime - (60 * 60) * hours

        return (0 until hours).toList()
            .parallelStream()
            .map { Pair(epoch + (60 * 60) * it, epoch + (60 * 60) * (it + 1)) }
            .map { get("#$code", it) }
            .map { it.size }.toList()
    }
}