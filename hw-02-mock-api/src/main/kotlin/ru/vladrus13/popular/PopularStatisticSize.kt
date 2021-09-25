package ru.vladrus13.popular

abstract class PopularStatisticSize : PopularStatistic {
    protected abstract fun get(code: String, time: Pair<Long, Long>): List<String>

    override fun get(code: String, hours: Int, currentTime : Long): List<Int> {
        if (hours <= 0) return emptyList()
        val epoch = currentTime - (60 * 60) * hours

        return (0 until hours).toList()
            .parallelStream()
            .map { Pair(epoch + (60 * 60) * it, epoch + (60 * 60) * (it + 1)) }
            .map { get("#$code", it) }
            .map { it.size }.toList()
    }
}