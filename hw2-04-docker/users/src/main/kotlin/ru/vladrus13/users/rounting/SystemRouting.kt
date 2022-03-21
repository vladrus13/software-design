package ru.vladrus13.users.rounting

import com.fasterxml.jackson.databind.JsonNode
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.vladrus13.common.bean.User
import ru.vladrus13.common.utils.*
import ru.vladrus13.users.database.InMemoryUsersDatabase
import ru.vladrus13.users.database.PromotionRepository

fun getJson(user: User): String {
    val root = objectMapper.createObjectNode()
    root.put("id", user.id)
    root.put("balance", user.balance)
    root.set<JsonNode>("promotions", user.promotions.let {
        val r = objectMapper.createObjectNode()
        for (p in it) {
            r.put(p.key.toString(), p.value.toString())
        }
        r
    })
    return objectWriter.writeValueAsString(root)
}

suspend fun ApplicationCall.respondJson(string: String) {
    this.respondText(string, contentType = ContentType.Application.Json)
}

fun Application.module() {

    val users = InMemoryUsersDatabase()
    val promotions = PromotionRepository("http://localhost:8080", users)

    routing {
        get("/user") {
            val userId = call.getParameter<Long>("userId", true)!!
            val user = users.findById(userId)
            if (user == null) {
                call.respondJson(error())
            } else {
                call.respondJson(getJson(user))
            }
        }

        get("/user/money") {
            val userId = call.getParameter<Long>("userId", true)!!
            val user = users.findById(userId)
            if (user == null) {
                call.respondJson(error())
            } else {
                var money = 0.0
                for (p in user.promotions) {
                    money += promotions.getPromotion(p.key).price * p.value
                }
                call.respondJson(result(money.toString()))
            }
        }

        get("/users/register") {
            val count = users.count().toLong()
            if (users.save(User(count))) {
                call.respondJson(result(count.toString()))
            } else {
                call.respondJson(error())
            }
        }
        get("/users/process") {
            val userId = call.getParameter<Long>("userId", true)!!
            val processId = call.getParameter<Long>("promotionId", true)!!
            val amount = call.getParameter<Long>("amount", true)!!
            if (promotions.operate(userId, processId, amount)) {
                call.respondJson(successful())
            } else {
                call.respondJson(error())
            }
        }
        get("/users/add") {
            val id = call.getParameter<Long>("id", true)!!
            val amount = call.getParameter<Double>("amount", true)!!
            users.add(id, amount)
            call.respondJson(successful())
        }
    }
}