package ru.vladrus13.actors.api.weather

import ru.vladrus13.apiMaster.APIMaster
import ru.vladrus13.apiMaster.requests.Request
import ru.vladrus13.apiMaster.response.Response
import ru.vladrus13.apiMaster.response.ResponseParser
import ru.vladrus13.apiMaster.response.json.JsonParser
import ru.vladrus13.apiMaster.response.json.annotation.Jsonable
import java.util.*

object OpenWeatherMap : APIMaster("https://api.openweathermap.org/data/2.5", 100), WeatherGetter {
    override val parser: ResponseParser<*> = JsonParser()

    private val apiKey = run {
        val properties = Properties()
        properties.load(WeatherApi::class.java.getResourceAsStream("/keys.properties"))
        properties.getProperty("openWeatherApi")!!
    }

    class Weather(
        @Jsonable(name = "main", isRequired = true)
        val main: Main
    ) {
        class Main(
            @Jsonable(name = "temp", isRequired = true)
            val temp: Double
        )
    }

    override fun getResult(query: String): Double {
        val response = execute(
            Request.Get("/weather").apply {
                addKey("appid", apiKey)
                addKey("lat", "35")
                addKey("lon", "139")
                addKey("units", "metric")
            },
            Weather::class,
            emptyList()
        )
        when (response) {
            is Response.ParseException, is Response.ServerException -> {
                throw Exception("Can't")
            }
            is Response.SuccessfulResponse -> {
                return response.data.main.temp
            }
        }
    }
}