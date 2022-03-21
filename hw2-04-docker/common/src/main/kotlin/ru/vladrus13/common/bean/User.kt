package ru.vladrus13.common.bean

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Long,
    var balance: Double = 0.0,
    val promotions: MutableMap<Long, Long> = mutableMapOf()
)