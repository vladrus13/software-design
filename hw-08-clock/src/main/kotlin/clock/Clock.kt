package clock

import java.time.Instant

/**
 * Abstract clock. Get gets time
 */
interface Clock {
    /**
     * Get time from clock
     *
     * @return time
     */
    fun now(): Instant
}