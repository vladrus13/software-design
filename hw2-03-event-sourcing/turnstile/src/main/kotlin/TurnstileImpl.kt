import ru.vladrus13.bean.TicketEvent
import ru.vladrus13.interfaces.EventStore
import ru.vladrus13.interfaces.Turnstile
import java.time.Instant

class TurnstileImpl : Turnstile {

    var eventStore : EventStore? = null

    fun bind(eventStore: EventStore) {
        this.eventStore = eventStore
    }

    override fun isAccepted(userId: Long, event: Instant): Boolean {
        val ticket = eventStore?.getUser(userId)
        return ticket?.ticketUntil == null || ticket.ticketUntil!! < event
    }

    override fun enter(userId: Long, isEnter: Boolean, event: Instant): Boolean {
        val ticket = eventStore?.getUser(userId)
        if (ticket?.ticketUntil == null || ticket.ticketUntil!! < event) {
            return false
        } else {
            eventStore?.enter(userId, isEnter)
            return true
        }
    }

}