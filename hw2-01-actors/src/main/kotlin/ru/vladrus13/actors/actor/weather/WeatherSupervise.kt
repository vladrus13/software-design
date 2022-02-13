package ru.vladrus13.actors.actor.weather

import akka.actor.UntypedAbstractActor
import ru.vladrus13.actors.actor.AbstractSupervise
import kotlin.reflect.KClass

class WeatherSupervise : AbstractSupervise<Double, String>() {
    override val id: String = "Weather supervise"
    override val classes: Map<String, Pair<KClass<out UntypedAbstractActor>, List<Any>>> =
        mutableMapOf(
            "Open weather actor" to Pair(OpenWeatherActor::class, listOf()),
            "API weather actor" to Pair(WeatherApi::class, listOf()),
            "Stack weather actor" to Pair(WeatherStack::class, listOf())
        )
    override val timeout: Long = 50000L
    override val aggregator: (List<Pair<String, Double?>>) -> String =
        { it.joinToString(separator = "\n") { "${it.first}: ${it.second}" } }
    override val code: String = "start"
}