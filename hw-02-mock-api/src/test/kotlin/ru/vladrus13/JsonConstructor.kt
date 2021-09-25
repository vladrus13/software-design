package ru.vladrus13

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive

class JsonConstructor {
    companion object {
        fun getTexts() : JsonObject {
            return JsonObject().apply {
                add("data", JsonArray().apply {
                    add(JsonObject().apply {
                        add("selftext", JsonPrimitive("Hello!"))
                    })
                })
            }
        }
    }
}