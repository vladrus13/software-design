package ru.vladrus13.actors.api.user

import ru.vladrus13.apiMaster.APIMaster
import ru.vladrus13.apiMaster.requests.Request
import ru.vladrus13.apiMaster.response.Response
import ru.vladrus13.apiMaster.response.ResponseParser
import ru.vladrus13.apiMaster.response.json.JsonParser
import ru.vladrus13.apiMaster.response.json.annotation.Jsonable

object Gitlabs : APIMaster("https://gitlab.com/api/v4", 100), UserGetter {
    override val parser: ResponseParser<*> = JsonParser()

    class User(
        @Jsonable(name = "login", isRequired = true)
        val login: String
    )

    override fun getResult(user: String): Boolean {
        val response = execute(
            Request.Get("/users").apply {
                addKey("username", user)
            },
            User::class,
            emptyList()
        )
        when (response) {
            is Response.ParseException, is Response.ServerException -> {
                return false
            }
            is Response.SuccessfulResponse -> {
                return true
            }
        }
    }
}