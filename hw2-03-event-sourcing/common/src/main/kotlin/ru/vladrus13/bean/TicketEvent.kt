package ru.vladrus13.bean

import java.time.Instant
import kotlin.time.Duration

sealed class TicketEvent(val userId: Long, val type: TicketEventType) {
    enum class TicketEventType {
        ADD, UPDATE, USE
    }

    class AddTicketEvent(userId: Long, val time: Instant) : TicketEvent(userId, TicketEventType.ADD)

    class UpdateTicketEvent(userId: Long, val time : Instant, val duration: Duration) : TicketEvent(userId, TicketEventType.UPDATE)

    class UseTicketEvent(userId: Long, val time: Instant, val isEnter: Boolean) :
        TicketEvent(userId, TicketEventType.USE)
}