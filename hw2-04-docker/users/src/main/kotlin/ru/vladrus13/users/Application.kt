package ru.vladrus13.users

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.vladrus13.users.rounting.module

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}