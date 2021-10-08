package ru.vladrus13.routing

import io.ktor.application.*
import kotlinx.html.*
import io.ktor.html.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.vladrus13.data.DatabaseManager
import ru.vladrus13.model.Note

fun Application.addRouting() {
    routing {
        get("/noteAdd") {
            call.respondHtml {
                head {
                    title { +"K! Adding notes"}
                }
                body {
                    h2 {
                        +"Добавление заметок!"
                    }
                    form(method = FormMethod.post, encType = FormEncType.multipartFormData) {
                        +"Название: "
                        textInput(name = "title"){
                            maxLength = 60.toString()
                            required = true
                        }
                        br()
                        +"Текст:"
                        br()
                        textArea(rows = "5") {
                            name = "text"
                        }
                        br()
                        +"Приоритет:"
                        select {
                            this.name = "priority"
                            for (i in (0..9)) {
                                option {
                                    value = i.toString()
                                    text(i)
                                }
                            }
                        }
                        br()
                        br()
                        submitInput(classes = "pure-button pure-button-primary") {
                            value = "Создать"
                        }
                    }
                }
            }
        }

        post("/noteAdd") {
            val parameters = call.receiveParameters()
            val title = parameters["title"]!!
            val text = parameters["text"]!!
            val priority = parameters["priority"]!!.toInt()
            val note = Note(
                title = title,
                text = text,
                priority = priority)
            DatabaseManager.put(note)
            call.respondRedirect(url = "/note/${note.id}")
        }
    }
}