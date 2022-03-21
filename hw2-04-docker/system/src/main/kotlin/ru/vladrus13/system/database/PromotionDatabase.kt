package ru.vladrus13.system.database

import ru.vladrus13.common.bean.Promotion

interface PromotionDatabase {
    fun findById(id: Long): Promotion?
    fun save(promotion: Promotion): Boolean
    fun changeCount(id: Long, change: Long): Boolean
    fun changePrice(id: Long, price: Double): Boolean
    fun getAll(): List<Promotion>
    fun count(): Int
}