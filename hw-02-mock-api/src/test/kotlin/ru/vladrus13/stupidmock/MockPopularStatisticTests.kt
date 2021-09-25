package ru.vladrus13.stupidmock

import org.junit.Test
import ru.vladrus13.TestUtils.Companion.assertList
import java.util.*

class MockPopularStatisticTests {

    @Test
    fun testEmpty() {
        val mockPopular = MockPopularStatistic(TreeSet<MockPopularStatistic.Text>())
        assertList(mockPopular.get("hello", 4, Date().time / 1000), listOf(0, 0, 0, 0))
        assertList(mockPopular.get("what?", 24, Date().time / 1000), MutableList(24) { 0 })
    }

    private fun getTime(time: Long, hourBefore: Int): Long {
        return time - hourBefore * (60 * 60) - 1
    }

    private fun text(text: String, date: Long, hourBefore: Int): MockPopularStatistic.Text {
        return MockPopularStatistic.Text(text, getTime(date, hourBefore))
    }

    @Test
    fun testOne() {
        val time = Date().time / 1000
        val mockPopular = MockPopularStatistic(
            TreeSet(
                listOf(
                    text("#hello, #ban!", time, 0)
                )
            )
        )
        assertList(mockPopular.get("hello", 4, time), listOf(0, 0, 0, 1))
        assertList(mockPopular.get("ban", 3, time), listOf(0, 0, 1))
        assertList(mockPopular.get("heh", 2, time), listOf(0, 0))
    }

    @Test
    fun testMany() {
        val time = Date().time / 1000
        val mockPopular = MockPopularStatistic(
            TreeSet(
                listOf(
                    text("#hello, #ban!", time, 0),
                    text("#hello, #ban!", time, 1),
                    text("#hello, #ban!", time, 2),
                    text("#hello, #ban!", time, 3),
                    text("#hello, #ban!!", time, 0),
                    text("#hello, #ban!!", time, 1),
                    text("#hello, #ban!!", time, 2),
                    text("#hello, #ban!!!", time, 0),
                    text("#hello, #ban!!!", time, 1),
                    text("#hello, #ban!!!!", time, 0)
                )
            )
        )
        assertList(mockPopular.get("hello", 4, time), listOf(1, 2, 3, 4))
        assertList(mockPopular.get("ban", 3, time), listOf(2, 3, 4))
        assertList(mockPopular.get("heh", 2, time), listOf(0, 0))
    }
}