package ru.vladrus13.actors.actor.weather

import ru.vladrus13.actors.actor.AbstractGetter
import ru.vladrus13.actors.api.Getter

class WeatherApi : AbstractGetter() {
    override val getter: Getter<*> = ru.vladrus13.actors.api.weather.WeatherApi
    override val id: String = "API weather actor"
}