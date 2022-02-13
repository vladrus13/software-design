package ru.vladrus13.actors.actor

import akka.actor.UntypedAbstractActor
import ru.vladrus13.actors.actor.user.UserSupervise
import ru.vladrus13.actors.actor.weather.WeatherSupervise
import kotlin.reflect.KClass

class RootSupervise : AbstractSupervise<String, String>() {
    override val id: String = "root supervise"
    override val classes: Map<String, Pair<KClass<out UntypedAbstractActor>, List<Any>>> =
        mutableMapOf(
            "Weather supervise" to Pair(WeatherSupervise::class, listOf()),
            "User supervise" to Pair(UserSupervise::class, listOf())
        )
    override val timeout: Long = 150000L
    override val aggregator: (List<Pair<String, String?>>) -> String = { it.joinToString(separator = "\n") }
    override val code: String = "start"

    override fun result(answer: String) {
        println(answer)
    }
}