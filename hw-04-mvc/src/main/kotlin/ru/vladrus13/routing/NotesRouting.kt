package ru.vladrus13.routing

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.response.*
import kotlinx.html.*
import io.ktor.routing.*
import ru.vladrus13.data.DatabaseManager


fun Application.notesRouting() {
    routing {
        get("/notes") {
            call.respondHtml {
                head {
                    title { +"K! Notes"}
                }
                body {
                    h2 {
                        +"Все заметки"
                    }
                    val notes = DatabaseManager.getAll()
                    notes.sortedBy {
                        it.priority
                    }.forEach {
                        +"Заглавие заметки: ${it.title}"
                        br()
                        +it.text
                        br()
                        br()
                    }
                }
            }
        }

        get("/note/{id}") {
            val id = call.parameters["id"]!!.toLong()
            val note = DatabaseManager.get(id)
            if (note == null) {
                call.respondText("Note with this id not found: $id")
            } else {
                call.respondHtml {
                    head {

                    }
                    body {
                        +"Заглавие заметки: ${note.title}"
                        br()
                        +note.text
                        br()
                        br()
                    }
                }
            }
        }
    }
}