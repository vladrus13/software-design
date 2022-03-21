package ru.vladrus13.system.database

import ru.vladrus13.common.bean.Promotion
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock

class InMemoryPromotionsDatabase : PromotionDatabase {

    private val promotions = ConcurrentHashMap<Long, Pair<ReentrantLock, Promotion>>()

    override fun findById(id: Long): Promotion? {
        return promotions[id]?.let {
            it.first.lock()
            val promotion = it.second
            it.first.unlock()
            return promotion
        }
    }

    override fun save(promotion: Promotion): Boolean {
        return if (promotions.contains(promotion.id)) {
            false
        } else {
            promotions[promotion.id] = Pair(ReentrantLock(), promotion)
            true
        }
    }

    override fun changeCount(id: Long, change: Long): Boolean {
        return if (promotions.contains(id)) {
            false
        } else {
            val p = promotions[id]
            if (p == null) {
                false
            } else {
                p.first.lock()
                if (p.second.amount + change < 0) {
                    p.first.unlock()
                    false
                } else {
                    p.second.amount += change
                    p.first.unlock()
                    true
                }
            }
        }
    }

    override fun changePrice(id: Long, price: Double): Boolean {
        return if (promotions.contains(id)) {
            false
        } else {
            val p = promotions[id]
            if (p == null) {
                false
            } else {
                p.first.lock()
                p.second.price = price
                p.first.unlock()
                true
            }
        }
    }

    override fun getAll(): List<Promotion> {
        return promotions.values.map { it.second }
    }

    override fun count(): Int = promotions.size
}