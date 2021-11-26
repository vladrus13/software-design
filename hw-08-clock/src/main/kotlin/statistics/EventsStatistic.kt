package statistics

/**
 * Counting events
 *
 */
interface EventsStatistic {
    /**
     * Add event to system
     *
     * @param name name of event
     */
    fun incEvent(name : String)

    /**
     * Get RPM of event
     *
     * @param name name of event
     * @return RPM of event
     */
    fun getEventByStatisticByName(name : String) : Double

    /**
     * Get all RPMs of events
     *
     * @return
     */
    fun getAllEventStatistic() : Map<String, Double>

    /**
     * Print statistic to console
     *
     */
    fun printStatistic()
}