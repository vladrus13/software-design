package ru.vladrus13.interfaces

import ru.vladrus13.bean.TicketEvent
import java.time.Instant

interface Turnstile {
    fun isAccepted(userId : Long, event: Instant = Instant.now()) : Boolean

    fun enter(userId: Long, isEnter: Boolean, event: Instant = Instant.now()) : Boolean
}