package ru.vladrus13.routing

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.vladrus13.data.DatabaseManager

fun Application.manageNoteRoutes() {
    routing {
        post("/delete/{id}") {
            val id = call.parameters["id"]!!.toLong()
            DatabaseManager.delete(id)
            call.respondRedirect("http://localhost:8080/notes")
        }

        post("/switch/{id}") {
            val id = call.parameters["id"]!!.toLong()
            val note = DatabaseManager.get(id)
            if (note != null) {
                note.isDone = true
                DatabaseManager.put(note)
            }
            call.respondRedirect("http://localhost:8080/notes")
        }
    }
}