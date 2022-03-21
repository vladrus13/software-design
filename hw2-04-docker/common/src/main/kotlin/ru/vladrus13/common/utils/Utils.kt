package ru.vladrus13.common.utils

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.application.*

val objectMapper = ObjectMapper()
val objectWriter = objectMapper.writer(DefaultPrettyPrinter())

inline fun <reified T : Any> ApplicationCall.getParameter(name: String, required: Boolean = false): T? {
    val value = request.queryParameters[name]
    return if (value == null) {
        if (required) {
            throw IllegalStateException("Required parameter failed")
        } else {
            null
        }
    } else {
        when (T::class) {
            String::class -> value as T
            Double::class -> value.toDouble() as T
            Long::class -> value.toLong() as T
            else -> throw IllegalStateException("Can't find this type: ${T::class.qualifiedName}")
        }
    }
}

fun result(r: String): String = objectWriter.writeValueAsString(objectMapper.createObjectNode().put("result", r))

fun successful(): String = result("successful")

fun error(): String = result("error")