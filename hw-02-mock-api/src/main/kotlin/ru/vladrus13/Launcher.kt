package ru.vladrus13

import ru.vladrus13.vk.VkPopularStatistic
import java.util.*

fun main() {
    val x = VkPopularStatistic().get("tasks", 24, Date().time / 1000)
    val y = 0
}