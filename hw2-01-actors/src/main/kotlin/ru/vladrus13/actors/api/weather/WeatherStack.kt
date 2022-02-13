package ru.vladrus13.actors.api.weather

import ru.vladrus13.apiMaster.APIMaster
import ru.vladrus13.apiMaster.requests.Request
import ru.vladrus13.apiMaster.response.Response
import ru.vladrus13.apiMaster.response.ResponseParser
import ru.vladrus13.apiMaster.response.json.JsonParser
import ru.vladrus13.apiMaster.response.json.annotation.Jsonable
import java.util.*

object WeatherStack : APIMaster("http://api.weatherstack.com", 100), WeatherGetter {
    override val parser: ResponseParser<*> = JsonParser()

    private val apiKey = run {
        val properties = Properties()
        properties.load(WeatherApi::class.java.getResourceAsStream("/keys.properties"))
        properties.getProperty("stackWeather")!!
    }

    class Weather(
        @Jsonable(name = "current", isRequired = true)
        val current: Current
    ) {
        class Current(
            @Jsonable(name = "temperature", isRequired = true)
            val temp: Double
        )
    }

    override fun getResult(query: String): Double {
        val response = execute(
            Request.Get("/current").apply {
                addKey("access_key", apiKey)
                addKey("query", query)
                addKey("units", "m")
            },
            Weather::class,
            emptyList()
        )
        when (response) {
            is Response.ParseException, is Response.ServerException -> {
                throw Exception("Can't")
            }
            is Response.SuccessfulResponse -> {
                return response.data.current.temp
            }
        }
    }
}