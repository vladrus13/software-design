package ru.vladrus13.actors.actor.weather

import ru.vladrus13.actors.actor.AbstractGetter
import ru.vladrus13.actors.api.Getter
import ru.vladrus13.actors.api.weather.OpenWeatherMap

class OpenWeatherActor : AbstractGetter() {
    override val getter: Getter<*> = OpenWeatherMap
    override val id: String = "Open weather actor"
}