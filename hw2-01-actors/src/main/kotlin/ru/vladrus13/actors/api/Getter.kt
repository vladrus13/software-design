package ru.vladrus13.actors.api

interface Getter<T> {
    fun getResult(query: String): T
}