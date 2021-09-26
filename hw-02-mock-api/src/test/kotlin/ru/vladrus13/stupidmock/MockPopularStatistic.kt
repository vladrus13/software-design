package ru.vladrus13.stupidmock

import ru.vladrus13.popular.PopularStatisticSize
import java.util.*

class MockPopularStatistic(val set: TreeSet<Text>) : PopularStatisticSize() {

    data class Text(val text: String = "", val time: Long) : Comparable<Text> {
        override operator fun compareTo(other: Text): Int {
            return if (this.time.compareTo(other.time) == 0) {
                this.text.compareTo(other.text)
            } else {
                this.time.compareTo(other.time)
            }
        }
    }

    override fun get(code: String, time: Pair<Long, Long>): List<String> {
        return set.subSet(Text(time = time.first), Text(time = time.second))
            .map {
                it.text
            }
            .filter {
                it.contains(code)
            }
            .toList()
    }
}