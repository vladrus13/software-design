package ru.vladrus13.system.routing

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.vladrus13.common.bean.Promotion
import ru.vladrus13.common.utils.*
import ru.vladrus13.system.database.InMemoryPromotionsDatabase

fun getJson(promotion: Promotion): String {
    val root = objectMapper.createObjectNode()
    root.put("name", promotion.name)
    root.put("amount", promotion.amount)
    root.put("price", promotion.price)
    return objectWriter.writeValueAsString(root)
}

fun getJson(promotions: List<Promotion>): String {
    val root = objectMapper.createArrayNode()
    for (promotion in promotions) {
        root.add(getJson(promotion))
    }
    return objectWriter.writeValueAsString(root)
}

val database = InMemoryPromotionsDatabase()

fun Application.module() {

    routing {
        get("/") {
            val id = call.getParameter<Long>("id", false)
            if (id != null) {
                val finded = database.findById(id)
                if (finded == null) {
                    call.respondText(error(), contentType = ContentType.Application.Json)
                } else {
                    call.respondText(getJson(finded), contentType = ContentType.Application.Json)
                }
            } else {
                call.respond(getJson(database.getAll()))
            }
        }
        get("/promotion/register") {
            val name = call.getParameter<String>("name", true)!!
            val amount = call.getParameter<Long>("amount", true)!!
            val price = call.getParameter<Double>("price", true)!!
            val size = database.count().toLong()
            if (database.save(Promotion(size, name, amount, price))) {
                call.respondText(result(size.toString()), contentType = ContentType.Application.Json)
            } else {
                call.respondText(error(), contentType = ContentType.Application.Json)
            }
        }
        get("/promotion/amount") {
            val id = call.getParameter<Long>("id", true)!!
            val amount = call.getParameter<Long>("amount", true)!!
            if (database.changeCount(id, amount)) {
                call.respondText(successful(), contentType = ContentType.Application.Json)
            } else {
                call.respondText(error(), contentType = ContentType.Application.Json)
            }
        }
        get("/promotion/price") {
            val id = call.getParameter<Long>("id", true)!!
            val price = call.getParameter<Double>("price", true)!!
            if (database.changePrice(id, price)) {
                call.respondText(successful(), contentType = ContentType.Application.Json)
            } else {
                call.respondText(error(), contentType = ContentType.Application.Json)
            }
        }
    }
}