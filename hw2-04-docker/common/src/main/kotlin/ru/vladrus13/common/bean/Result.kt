package ru.vladrus13.common.bean

import kotlinx.serialization.Serializable

@Serializable
data class Result<T>(val result: T)