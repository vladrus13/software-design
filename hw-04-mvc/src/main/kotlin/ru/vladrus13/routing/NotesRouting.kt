package ru.vladrus13.routing

import io.ktor.application.*
import io.ktor.html.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.html.*
import ru.vladrus13.data.DatabaseManager
import ru.vladrus13.model.Note
import ru.vladrus13.model.UserSession

fun note(note: Note, f: BODY) {
    f.div(classes = "note") {
        style = "background-color:${if (note.isDone) "green" else "yellow"}; max-width: 40rem; min-width: 40rem; border: 1px solid black"
        form(method = FormMethod.post, encType = FormEncType.multipartFormData, action = "/delete/${note.id}") {
            id = "delete${note.id}"
        }
        form(method = FormMethod.post, encType = FormEncType.multipartFormData, action = "/switch/${note.id}") {
            id = "switch${note.id}"
        }
        table {
            tr {
                th {
                    style = "max-width: 20rem; min-width: 20rem; text-align: left; word-wrap: break-word"
                    +note.title
                }
                th {
                    style = "max-width: 8rem; min-width: 8rem"
                    +"Приоритет: ${note.priority}"
                }
                th {
                    style = "max-width: 4rem; min-width: 4rem"
                    if (!note.isDone) {
                        submitInput(classes = "pure-button pure-button-primary") {
                            form = "switch${note.id}"
                            value = "Done"
                        }
                    }
                }
                th {
                    style = "max-width: 3rem; min-width: 3rem"
                    submitInput(classes = "pure-button pure-button-primary") {
                        form = "delete${note.id}"
                        value = "Ban!"
                    }
                }
            }
            tr {
                td {
                    style = "max-width: 20rem; min-width: 20rem; word-wrap: break-word"
                    +note.text
                }
            }
        }
    }
}

fun notes(f: BODY, selector: Comparator<Note>, filter: (Note) -> Boolean) {
    val notes = DatabaseManager.getAll()
    notes
        .filter(filter)
        .sortedWith(selector)
        .forEach {
            note(it, f)
            f.br()
        }
}

fun Application.notesRouting() {
    install(Sessions) {
        cookie<UserSession>("user_session")
    }
    routing {
        get("/notes") {
            val userSession = call.sessions.get<UserSession>() ?: UserSession()
            val sortBy: Comparator<Note> = when (userSession.sortBy) {
                "id" -> Comparator { n1, n2 -> n1.id!!.compareTo(n2.id!!) }
                "priority" -> Comparator { n1, n2 -> n1.priority.compareTo(n2.priority) }
                else -> Comparator { n1, n2 -> n1.id!!.compareTo(n2.id!!) }
            }
            val filter: (Note) -> Boolean = when (userSession.filter) {
                "done" -> {
                    {
                        it.isDone
                    }
                }
                "!done" -> {
                    {
                        !it.isDone
                    }
                }
                "both" -> {
                    {
                        true
                    }
                }
                else -> {
                    {
                        true
                    }
                }
            }
            call.respondHtml {
                head {
                    title { +"K! Notes" }
                }
                body {
                    h2 {
                        +"Все заметки"
                    }
                    div {
                        +"Сортировать: "
                        form(method = FormMethod.post, encType = FormEncType.multipartFormData, action = "sortBy/id") {
                            submitInput(classes = "pure-button pure-button-primary") {
                                value = "по id"
                            }
                        }
                        form(method = FormMethod.post, encType = FormEncType.multipartFormData, action = "sortBy/priority") {
                            submitInput(classes = "pure-button pure-button-primary") {
                                value = "по priority"
                            }
                        }
                    }
                    div {
                        +"Показывать: "
                        form(method = FormMethod.post, encType = FormEncType.multipartFormData, action = "filter/done") {
                            submitInput(classes = "pure-button pure-button-primary") {
                                value = "Сделанные"
                            }
                        }
                        form(method = FormMethod.post, encType = FormEncType.multipartFormData, action = "filter/!done") {
                            submitInput(classes = "pure-button pure-button-primary") {
                                value = "Несделанные"
                            }
                        }
                        form(method = FormMethod.post, encType = FormEncType.multipartFormData, action = "filter/both") {
                            submitInput(classes = "pure-button pure-button-primary") {
                                value = "Все"
                            }
                        }

                    }

                    notes(this, sortBy, filter)
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
                        note(note, this)
                    }
                }
            }
        }

        post("/sortBy/{by}") {
            val userSession = call.sessions.get<UserSession>() ?: UserSession()
            call.sessions.set(userSession.apply {
                sortBy = call.parameters["by"]!!
            })
            call.respondRedirect("/notes")
        }

        post("/filter/{by}") {
            val userSession = call.sessions.get<UserSession>() ?: UserSession()
            call.sessions.set(userSession.apply {
                filter = call.parameters["by"]!!
            })
            call.respondRedirect("/notes")
        }
    }
}