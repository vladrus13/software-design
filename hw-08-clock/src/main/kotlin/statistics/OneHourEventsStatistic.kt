package statistics

import clock.Clock
import java.util.*

/**
 * Contain only one hour statistic
 *
 */
class OneHourEventsStatistic(private val clock: Clock) : EventsStatistic {

    companion object {
        /**
         * Count seconds on hour
         */
        const val COUNT_SECONDS_ON_HOUR = 60 * 60L // TimeUnit.HOURS.toSeconds(1L)

        /**
         * Count minutes on hour
         */
        const val COUNT_MINUTES_ON_HOUR = 60L // TimeUnit.HOURS.toMinutes(1L)
    }

    private val events = mutableMapOf<String, LinkedList<Long>>()

    private fun clear(list: LinkedList<Long>) {
        while (list.isNotEmpty() && clock.now().epochSecond - list.first > COUNT_SECONDS_ON_HOUR) {
            list.removeFirst()
        }
    }

    override fun incEvent(name: String) {
        events.getOrPut(name) { LinkedList() }.apply { add(clock.now().epochSecond) }
    }

    override fun getEventByStatisticByName(name: String): Double {
        if (events.containsKey(name)) {
            val list = events[name]
            clear(list!!)
            return list.size.toDouble() / COUNT_MINUTES_ON_HOUR
        } else {
            return 0.0
        }
    }

    override fun getAllEventStatistic(): Map<String, Double> =
        events.keys.associateWith { getEventByStatisticByName(it) }

    override fun printStatistic() {
        println(events.map { (key, value) ->
            "Event: \"$key\", rpm: event ${value.apply { clear(this) }.size.toDouble() / COUNT_SECONDS_ON_HOUR} per minute"
        })
    }
}