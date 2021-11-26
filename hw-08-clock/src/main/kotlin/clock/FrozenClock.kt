package clock

import java.time.Instant

/**
 * Frozen clock, which just frozen
 *
 * @property froze time on which we froze
 */
class FrozenClock(private var froze: Instant) : Clock {
    override fun now(): Instant = froze

    /**
     * Add seconds to time
     *
     * @param time how much add
     */
    fun addSeconds(time : Long) {
        froze = froze.plusSeconds(time)
    }
}