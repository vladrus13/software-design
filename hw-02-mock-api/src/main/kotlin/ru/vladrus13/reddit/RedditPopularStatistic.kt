package ru.vladrus13.reddit

import org.json.JSONObject
import ru.vladrus13.popular.PopularStatisticSize
import java.net.URL

/**
 * Implementation of popular class with Reddit API
 *
 */
open class RedditPopularStatistic : PopularStatisticSize() {

    /**
     * Host of Reddit API
     */
    open val url = "https://api.pushshift.io/"

    private fun getQuery(start: Long, finish: Long, code: String): String {
        return "$url?reddit/search/submission/sort_type=created_utc&after=$start&before=$finish&size=500&q=$code"
    }

    public override fun get(code: String, time: Pair<Long, Long>): List<String> =
        URL(getQuery(time.first, time.second, code)).readText().let {
            JSONObject(it).getJSONArray("data")
        }.let {
            it.map { it1 ->
                (it1 as JSONObject).get("selftext").toString()
            }
        }
}