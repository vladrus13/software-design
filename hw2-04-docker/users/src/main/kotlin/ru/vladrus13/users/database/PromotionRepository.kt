package ru.vladrus13.users.database

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import ru.vladrus13.common.bean.Promotion

class PromotionRepository(
    private val url: String,
    private val database: UsersDatabase,
    private val client: HttpClient = HttpClient(Apache) {
        install(JsonFeature) {
            serializer = JacksonSerializer()
        }
    }
) {

    data class Result(val result: String)

    suspend fun operate(userId: Long, promotionId: Long, amount: Long): Boolean {
        val promotion: Promotion = client.get("$url/?id=$promotionId")
        return if (database.changePromotions(userId, amount, promotion)) {
            val result = client.get<Result>("${url}/promotion/amount?id=${promotionId}&amount=${-amount}")
            if (result.result == "successful") {
                true
            } else {
                database.changePromotions(userId, -amount, promotion)
                false
            }
        } else {
            false
        }
    }

    suspend fun getPromotion(promotionId: Long): Promotion {
        return client.get("$url/?id=$promotionId")
    }

}