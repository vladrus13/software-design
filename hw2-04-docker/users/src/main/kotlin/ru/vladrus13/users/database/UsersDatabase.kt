package ru.vladrus13.users.database

import ru.vladrus13.common.bean.Promotion
import ru.vladrus13.common.bean.User

interface UsersDatabase {
    fun findById(id: Long): User?
    fun save(user: User): Boolean
    fun changePromotions(userId: Long, amount: Long, promotion: Promotion): Boolean
    fun count(): Int
    fun add(userId: Long, amount: Double): Boolean
}