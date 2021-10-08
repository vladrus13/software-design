package ru.vladrus13.model

data class Note(var id: Long? = null, val title: String, val text: String, val priority: Int, var isDone: Boolean = false)