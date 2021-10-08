package ru.vladrus13.data

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import ru.vladrus13.model.Note

class DatabaseManager {
    companion object {
        private val connection = Database.connect(
            "jdbc:mysql://localhost:3306/softwaredesign4hw",
            driver = "com.mysql.cj.jdbc.Driver",
            user = "softwaredesign",
            password = "softwaredesign"
        )

        fun init() {
            transaction(connection) {
                SchemaUtils.create(Notes)
            }
        }

        object Notes : Table() {
            val id = long("note_id").uniqueIndex().autoIncrement().primaryKey()
            val title = varchar("title", 63)
            val text = text("text")
            val priority = integer("priority")
            val isDone = bool("is_done")
        }

        fun getAll() : List<Note> {
            val list = mutableListOf<Note>()
            transaction(connection) {
                val all = Notes.selectAll()
                list.addAll(all.map {
                    get(it)
                })
            }
            return list
        }


        private fun set(o : Note, it : UpdateBuilder<Number>) {
            it[Notes.title] = o.title
            it[Notes.text] = o.text
            it[Notes.priority] = o.priority
            it[Notes.isDone] = o.isDone
        }

        fun put(o : Note) {
            transaction(connection) {
                if (o.id == null) {
                    o.id = Notes.insert {
                        set(o, it)
                    }[Notes.id]
                    return@transaction
                }
                val entities = Notes.select { Notes.id eq o.id!! }
                if (entities.count() == 0) {
                    o.id = Notes.insert {
                        set(o, it)
                    }[Notes.id]
                } else {
                    Notes.update({Notes.id eq o.id!!}) {
                        set(o, it)
                    }
                }
            }
        }

        private fun get(resultRow: ResultRow) : Note = Note(
            resultRow[Notes.id], resultRow[Notes.title], resultRow[Notes.text], resultRow[Notes.priority], resultRow[Notes.isDone]
        )

        fun get(id : Long) : Note? {
            var note : Note? = null
            transaction(connection) {
                val entities = Notes.select { Notes.id eq id }
                note = when (entities.count()) {
                    0 -> null
                    1 -> get(entities.single())
                    else -> throw IllegalStateException("More than 1 entities with id: $id")
                }
            }
            return note
        }

        fun delete(id : Long) {
            transaction(connection) {
                Notes.deleteWhere { Notes.id eq id }
            }
        }
    }
}