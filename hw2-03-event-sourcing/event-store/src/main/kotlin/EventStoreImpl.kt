import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import de.undercouch.bson4jackson.BsonFactory
import org.bson.Document
import ru.vladrus13.bean.TicketEvent
import ru.vladrus13.bean.User
import ru.vladrus13.interfaces.EventStore
import ru.vladrus13.interfaces.ReportService
import kotlin.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.time.toJavaDuration

class EventStoreImpl(val reportService: ReportService) : EventStore {

    val jackson = ObjectMapper(BsonFactory())
        .findAndRegisterModules()
        .apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }

    fun ps(document: Document) : TicketEvent {
        return when (TicketEvent.TicketEventType.valueOf(document.get("type", String::class.java))) {
            TicketEvent.TicketEventType.USE -> jackson.convertValue<TicketEvent.UseTicketEvent>(document)
            TicketEvent.TicketEventType.ADD -> jackson.convertValue<TicketEvent.AddTicketEvent>(document)
            TicketEvent.TicketEventType.UPDATE -> jackson.convertValue<TicketEvent.UpdateTicketEvent>(document)
        }
    }

    val eventsCollection = reportService.getCollection()
    val users: MutableMap<Long, User> = run {
        eventsCollection
            .find()
            .toList()
            .map { ps(it) }
            .groupBy { it.userId }
            .mapValues {
                val zoneId = ZoneId.systemDefault()
                val list = it.value
                val last =
                    list
                        .mapNotNull { if (it is TicketEvent.UpdateTicketEvent) it else null }
                        .maxByOrNull { it.time.atZone(zoneId).toEpochSecond() }
                if (last == null) {
                    User(it.key, null)
                } else {
                    User(it.key, last.time.plus(java.time.Duration.of(last.durationMilliseconds, ChronoUnit.MILLIS)))
                }
            }
            .toMutableMap()
    }

    override fun newUser(event: Instant): Long {
        val id = users.size.toLong()
        val ticket = TicketEvent.AddTicketEvent(id, event)
        reportService.insertOne(ticket)
        users[id] = User(id, null)
        return id
    }

    override fun updateUser(id: Long, time: Long, event: Instant) {
        val ticket = TicketEvent.UpdateTicketEvent(id, event, time)
        reportService.insertOne(ticket)
        users[id] = User(id, event.plus(java.time.Duration.of(ticket.durationMilliseconds, ChronoUnit.MILLIS)))
    }

    override fun getUser(userId: Long): User? = users[userId]

    override fun enter(userId: Long, isEnter: Boolean, event: Instant) {
        reportService.insertOne(TicketEvent.UseTicketEvent(userId, event, isEnter))
    }
}