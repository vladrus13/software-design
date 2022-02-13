package ru.vladrus13.actors.actor.weather

import ru.vladrus13.actors.actor.AbstractGetter
import ru.vladrus13.actors.api.Getter

class WeatherStack : AbstractGetter() {
    override val getter: Getter<*> = ru.vladrus13.actors.api.weather.WeatherStack
    override val id: String = "Stack weather actor"
}