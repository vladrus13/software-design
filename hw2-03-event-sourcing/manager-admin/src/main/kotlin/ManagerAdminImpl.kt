import ru.vladrus13.bean.User
import ru.vladrus13.interfaces.EventStore
import ru.vladrus13.interfaces.ManagerAdmin
import java.time.Instant
import kotlin.time.Duration

class ManagerAdminImpl : ManagerAdmin {

    var eventStore: EventStore? = null

    fun bind(eventStore: EventStore) {
        this.eventStore = eventStore
    }

    override fun getUserById(id: Long): User? =
        eventStore?.getUser(id)

    override fun newUser(event : Instant) : Long? =
        eventStore?.newUser(event)

    override fun update(id: Long, time: Long, event : Instant) {
        if (eventStore != null) eventStore!!.updateUser(id, time, event)
    }
}