package ru.vladrus13.bean

import java.time.Instant

data class User(val id: Long, val ticketUntil : Instant? = null)