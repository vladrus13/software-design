package ru.vladrus13

import com.xebialabs.restito.builder.stub.StubHttp.whenHttp
import com.xebialabs.restito.semantics.Action
import com.xebialabs.restito.server.StubServer
import org.junit.Test
import ru.vladrus13.TestUtils.Companion.assertList
import ru.vladrus13.reddit.RedditPopularStatistic
import java.util.*

class PopularStatisticTest {
    private val redditPopularStatistic = object : RedditPopularStatistic() {
        override val url: String = "http://localhost:8098/"
    }

    @Test
    fun testOneEmpty() {
        val server = StubServer(8098)
        whenHttp(server).match().then(
            Action.stringContent(JsonConstructor.getTexts().toString())
        )
        server.run()
        redditPopularStatistic.get("hello", 1)
        server.stop()
    }

    @Test
    fun testStorm() =
        repeat(24) {
            val server = StubServer(8098)
            whenHttp(server).match().then(
                Action.stringContent(JsonConstructor.getTexts().toString())
            )
            server.run()
            redditPopularStatistic.get("hello", 24)
            server.stop()
        }

    @Test
    fun testEternity() {
        val server = StubServer(8098)
        whenHttp(server).match().then(
            Action.stringContent(JsonConstructor.getTexts().toString())
        )
        server.run()
        val date = Date().time
        val map: MutableMap<Int, List<Int>> = hashMapOf()
        (0..24).forEach { hours ->
            map[hours] = redditPopularStatistic.get("hello", hours, date)
        }
        (0..24).forEach { hours ->
            assertList(map[hours]!!, redditPopularStatistic.get("hello", hours, date))
        }
        server.stop()
    }
}