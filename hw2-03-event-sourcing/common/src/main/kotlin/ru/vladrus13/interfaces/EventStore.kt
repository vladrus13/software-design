package ru.vladrus13.interfaces

import ru.vladrus13.bean.User
import kotlin.time.Duration
import java.time.Instant

interface EventStore {
    fun newUser(event : Instant = Instant.now()) : Long
    fun updateUser(id: Long, time: Long, event: Instant = Instant.now())
    fun getUser(userId: Long): User?
    fun enter(userId: Long, isEnter: Boolean, event: Instant = Instant.now())
}