import clock.FrozenClock
import statistics.OneHourEventsStatistic
import java.time.Instant

/**
 * Main
 *
 */
fun main() {
    val clock = FrozenClock(Instant.ofEpochSecond(0))
    val stats = OneHourEventsStatistic(clock)
    stats.incEvent("hello")
    stats.incEvent("hello")
    stats.incEvent("not hello")
    stats.printStatistic()
    clock.addSeconds(60 * 60L + 1)
    stats.incEvent("bye")
    stats.incEvent("bye")
    stats.incEvent("not bye")
    stats.printStatistic()
}