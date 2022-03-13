import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Filters.eq
import de.undercouch.bson4jackson.BsonFactory
import org.bson.Document
import org.litote.kmongo.*
import ru.vladrus13.bean.TicketEvent
import ru.vladrus13.interfaces.ReportService
import java.time.Duration
import java.time.LocalDate
import java.time.Instant
import java.time.ZoneOffset

class ReportServiceImpl : ReportService {

    val jackson = ObjectMapper(BsonFactory())
        .findAndRegisterModules()
        .apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }

    fun ps(document: Document): TicketEvent {
        return when (TicketEvent.TicketEventType.valueOf(document.get("type", String::class.java))) {
            TicketEvent.TicketEventType.USE -> jackson.convertValue<TicketEvent.UseTicketEvent>(document)
            TicketEvent.TicketEventType.ADD -> jackson.convertValue<TicketEvent.AddTicketEvent>(document)
            TicketEvent.TicketEventType.UPDATE -> jackson.convertValue<TicketEvent.UpdateTicketEvent>(document)
        }
    }

    val database = KMongo.createClient()
    val events = database.getDatabase("hw2-03-event-sourcing")
    val eventsCollection = events.getCollection("tickets")

    override fun getStatistics(): Map<LocalDate, Long> =
        eventsCollection
            .find(
                and(
                    eq("type", TicketEvent.TicketEventType.USE.toString()),
                    eq("direction", true)
                )
            )
            .toList()
            .map { ps(it) }
            .filterIsInstance<TicketEvent.UseTicketEvent>()
            .groupBy { LocalDate.ofInstant(it.time, ZoneOffset.UTC) }
            .mapValues { it.value.size.toLong() }

    override fun avgCount(): Double =
        eventsCollection
            .find()
            .toList()
            .map { ps(it) }
            .filterIsInstance<TicketEvent.UseTicketEvent>()
            .groupBy { it.userId }
            .mapValues { entry ->
                var mulisecondsSum = 0L
                for (d in entry.value) {
                    when (d.isEnter) {
                        true -> {
                            mulisecondsSum += 1
                        }

                        false -> {
                        }
                    }
                }
                mulisecondsSum
            }
            .keys.let {
                if (it.isNotEmpty())
                    it.sum().toDouble() / it.size
                else 0.0
            }

    override fun avgDuration(): Double =
        eventsCollection
            .find()
            .toList()
            .map { ps(it) }
            .filterIsInstance<TicketEvent.UseTicketEvent>()
            .groupBy { it.userId }
            .mapValues { entry ->
                var enterDistance: Instant? = null
                var mulisecondsSum = 0L
                for (d in entry.value) {
                    when (d.isEnter) {
                        true -> {
                            enterDistance = d.time
                        }

                        false -> {
                            mulisecondsSum += Duration.between(d.time, enterDistance!!).toMillis() / 1000
                        }
                    }
                }
                mulisecondsSum
            }
            .keys.let {
                if (it.isNotEmpty())
                    it.sum().toDouble() / it.size
                else 0.0
            }

    override fun getCollection(): MongoCollection<Document> = eventsCollection

    override fun insertOne(event: TicketEvent) {
        eventsCollection.insertOne(jackson.convertValue(event))
    }
}