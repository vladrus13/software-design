package ru.vladrus13.vk

import com.google.gson.Gson
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.client.actors.ServiceActor
import com.vk.api.sdk.httpclient.HttpTransportClient
import ru.vladrus13.popular.PopularStatisticSize
import ru.vladrus13.reddit.RedditPopularStatistic
import java.util.*

/**
 * Implementation of popular class with VK API
 *
 */
class VkPopularStatistic : PopularStatisticSize() {

    private val vkApiClient: VkApiClient
    private val actor: ServiceActor

    init {
        val transportClient = HttpTransportClient.getInstance()
        vkApiClient = VkApiClient(transportClient, Gson(), 5)
        val properties = Properties()
        properties.load(RedditPopularStatistic::class.java.getResourceAsStream("/vk.properties"))
        actor = ServiceActor(properties["APP_ID"].toString().toInt(), properties["SERVICE_TOKEN"] as String)
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+3"))
    }

    override fun get(code: String, time: Pair<Long, Long>): List<String> =
        vkApiClient
            .newsfeed()
            .search(actor).count(200)
            .startTime(time.first.toInt()).endTime(time.second.toInt())
            .q(code).execute().let {
                it.items
                    .map { it2 -> it2.text }
            }
}