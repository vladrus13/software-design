package ru.vladrus13

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.vladrus13.data.DatabaseManager
import ru.vladrus13.routing.addRouting
import ru.vladrus13.routing.notesRouting

fun main() {
    DatabaseManager.init()
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        addRouting()
        notesRouting()
    }.start(wait = true)
}
