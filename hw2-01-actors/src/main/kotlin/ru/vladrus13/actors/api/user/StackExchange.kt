package ru.vladrus13.actors.api.user

import ru.vladrus13.apiMaster.APIMaster
import ru.vladrus13.apiMaster.requests.Request
import ru.vladrus13.apiMaster.response.Response
import ru.vladrus13.apiMaster.response.ResponseParser
import ru.vladrus13.apiMaster.response.json.JsonParser
import ru.vladrus13.apiMaster.response.json.annotation.Jsonable

object StackExchange : APIMaster("https://api.stackexchange.com/2.3", 100), UserGetter {
    override val parser: ResponseParser<*> = JsonParser()

    class Users(
        @Jsonable(name = "items", isRequired = true)
        val users: ArrayList<User>
    ) {
        class User(
            @Jsonable(name = "account_id", isRequired = true)
            val id: Int
        )
    }

    override fun getResult(user: String): Boolean {
        val response = execute(
            Request.Get("/users").apply {
                addKey("order", "desc")
                addKey("sort", "reputation")
                addKey("inname", user)
                addKey("site", "stackoverflow")
            },
            Users::class,
            emptyList()
        )
        when (response) {
            is Response.ParseException, is Response.ServerException -> {
                return false
            }
            is Response.SuccessfulResponse -> {
                return response.data.users.isNotEmpty()
            }
        }
    }
}