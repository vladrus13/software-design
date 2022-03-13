package ru.vladrus13.interfaces

import ru.vladrus13.bean.User
import java.time.Instant
import kotlin.time.Duration

interface ManagerAdmin {
    fun getUserById(id : Long) : User?
    fun newUser(event : Instant = Instant.now()) : Long?
    fun update(id : Long, time : Long, event : Instant = Instant.now())
}