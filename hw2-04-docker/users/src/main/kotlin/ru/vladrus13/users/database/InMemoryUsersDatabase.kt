package ru.vladrus13.users.database

import ru.vladrus13.common.bean.Promotion
import ru.vladrus13.common.bean.User
import java.util.concurrent.ConcurrentHashMap

class InMemoryUsersDatabase : UsersDatabase {

    private val users = ConcurrentHashMap<Long, User>()

    override fun findById(id: Long): User? {
        return users[id]
    }

    override fun save(user: User): Boolean {
        users[user.id] = user
        return true
    }

    override fun changePromotions(userId: Long, amount: Long, promotion: Promotion): Boolean {
        val user = users[userId]
        return if (user == null) {
            false
        } else {
            val ch = amount * promotion.price
            if (user.promotions[promotion.id] == null) {
                user.promotions[promotion.id] = 0
            }
            if (user.balance + ch < 0 || user.promotions[promotion.id]!! + amount < 0) {
                false
            } else {
                user.promotions[promotion.id] = user.promotions[promotion.id]!! + amount
                user.balance += ch
                true
            }
        }
    }

    override fun count(): Int {
        return users.size
    }

    override fun add(userId: Long, amount: Double): Boolean {
        val user = users[userId]
        return if (user == null) {
            false
        } else {
            user.balance += amount
            true
        }
    }
}