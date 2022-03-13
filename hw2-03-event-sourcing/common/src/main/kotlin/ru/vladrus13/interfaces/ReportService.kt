package ru.vladrus13.interfaces

import com.mongodb.client.MongoCollection
import org.bson.Document
import ru.vladrus13.bean.TicketEvent
import java.time.LocalDate

interface ReportService {
    fun getStatistics() : Map<LocalDate, Long>
    fun avgCount() : Double
    fun avgDuration() : Double

    fun getCollection() : MongoCollection<Document>

    fun insertOne(event : TicketEvent)
}