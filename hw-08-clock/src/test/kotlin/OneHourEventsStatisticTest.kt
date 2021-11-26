import clock.FrozenClock
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import statistics.OneHourEventsStatistic
import java.time.Instant
import kotlin.math.abs

class OneHourEventsStatisticTest {
    private var clock = FrozenClock(Instant.ofEpochSecond(0))
    private var eventsStatistic = OneHourEventsStatistic(clock)
    private val COUNT_MINUTES_ON_HOUR = 60L
    private val COUNT_SECONDS_ON_HOUR = 60 * 60L

    @BeforeEach
    fun before() {
        clock = FrozenClock(Instant.ofEpochSecond(0))
        eventsStatistic = OneHourEventsStatistic(clock)
    }

    private fun Double.compareRPM(original: Int) = Assertions.assertTrue(
        abs(this * COUNT_MINUTES_ON_HOUR - original) < 1e-3,
        "Wrong count! RPM: $this, Expected: $original"
    )

    private fun String.compareRpm(original: Int) =
        eventsStatistic.getEventByStatisticByName(this).compareRPM(original)

    private fun String.add() = eventsStatistic.incEvent(this)

    private fun clearClock() = clock.addSeconds(COUNT_SECONDS_ON_HOUR + 1)

    @Test
    fun testEmpty() {
        "hello".compareRpm(0)
        "bye".compareRpm(0)
    }

    @Test
    fun testOne() {
        "hello".add()
        "hello".compareRpm(1)
        "heh".compareRpm(0)
    }

    @Test
    fun testClear() {
        "hello".add()
        "hello".compareRpm(1)
        clearClock()
        "hello".compareRpm(0)
    }

    @Test
    fun testBig() {
        val events = listOf("1" to 1, "2" to 2, "4" to 4, "183" to 183)
        for (event in events) {
            for (count in 0 until event.second) {
                event.first.add()
            }
        }
        for (event in events) {
            event.first.compareRpm(event.second)
        }
    }

    @Test
    fun testClearBig() {
        for (i in (1..10)) {
            val events = (1..1000).map {
                it.toString() to (0..it).random()
            }
            for (event in events) {
                for (count in 0 until event.second) {
                    event.first.add()
                }
            }
            for (event in events) {
                event.first.compareRpm(event.second)
            }
            clearClock()
        }
    }
}