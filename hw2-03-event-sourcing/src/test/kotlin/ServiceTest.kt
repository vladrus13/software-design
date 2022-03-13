import com.mongodb.client.model.Filters
import org.junit.jupiter.api.Test
import org.litote.kmongo.KMongo
import org.litote.kmongo.getCollection
import ru.vladrus13.bean.TicketEvent
import ru.vladrus13.interfaces.EventStore
import ru.vladrus13.interfaces.ManagerAdmin
import ru.vladrus13.interfaces.ReportService
import ru.vladrus13.interfaces.Turnstile
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAmount
import java.time.temporal.TemporalUnit
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.time.toJavaDuration
import kotlin.time.toKotlinDuration

class ServiceTest {

    fun clean() {
        val database = KMongo.createClient()
        val events = database.getDatabase("hw2-03-event-sourcing")
        val eventsCollection = events.getCollection<TicketEvent>("tickets")
        eventsCollection.deleteMany(Filters.empty())
    }

    data class State(
        val reportService: ReportService,
        val eventStore: EventStore,
        val turnstiles: List<Turnstile>,
        val managers: List<ManagerAdmin>
    ) {
        companion object {
            fun getState(turnstiles: Int, managers: Int): State {
                val reportService = ReportServiceImpl()
                val eventService = EventStoreImpl(reportService)
                val turnstilesList = (1..turnstiles).map {
                    val turnstile = TurnstileImpl()
                    turnstile.bind(eventService)
                    turnstile
                }
                val managersList = (1..managers).map {
                    val managerAdmin = ManagerAdminImpl()
                    managerAdmin.bind(eventService)
                    managerAdmin
                }
                return State(reportService, eventService, turnstilesList, managersList)
            }
        }
    }

    @Test
    fun `empty test`() {
        clean()
        val state = State.getState(1, 1)
    }

    @Test
    fun `add one ticket`() {
        clean()
        val state = State.getState(1, 1)
        state.managers.first().newUser()
    }

    @Test
    fun `add one ticket and enter`() {
        clean()
        val startTime = Instant.now()
        val state = State.getState(1, 1)
        val id = state.managers.first().newUser(startTime)
        assertNotNull(id)
        assert(state.turnstiles.first().isAccepted(id, startTime.plus(Duration.of(1, ChronoUnit.SECONDS))))
        state.managers.first().update(
            id,
            10000,
            startTime.plus(Duration.of(2, ChronoUnit.SECONDS))
        )
        assert(!state.turnstiles.first().isAccepted(id, startTime.plus(Duration.of(3, ChronoUnit.SECONDS))))
        assert(state.turnstiles.first().isAccepted(id, startTime.plus(Duration.of(30, ChronoUnit.SECONDS))))
    }

    @Test
    fun `add long ticket and attack`() {
        clean()
        val startTime = Instant.now()
        val state = State.getState(10, 1)
        val users = (0..9).map {
            val id = state.managers.first().newUser(startTime)
            assertNotNull(id)
            id
        }
        users.forEach {
            assert(state.turnstiles.first().isAccepted(it, startTime.plus(Duration.of(1, ChronoUnit.SECONDS))))
        }
        users.forEach {
            state.managers.first().update(
                it,
                10000,
                startTime.plus(Duration.of(2, ChronoUnit.SECONDS))
            )
        }
        (0..100).forEach {
            state.turnstiles[it % 10].enter(
                it % 10L,
                true,
                startTime.plus(Duration.of(1, ChronoUnit.SECONDS)))
            state.turnstiles[it % 10].enter(
                it % 10L,
                false,
                startTime.plus(Duration.of(1, ChronoUnit.SECONDS)))
        }
        assertEquals(4.5, ReportServiceImpl().avgCount())
        assertEquals(4.5, ReportServiceImpl().avgDuration())
    }
}