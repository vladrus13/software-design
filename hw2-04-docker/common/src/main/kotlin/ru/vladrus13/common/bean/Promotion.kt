package ru.vladrus13.common.bean

import kotlinx.serialization.Serializable

@Serializable
data class Promotion(
    val id: Long,
    val name: String,
    var amount: Long,
    var price: Double
)