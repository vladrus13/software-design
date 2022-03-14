fun main() {
    val reportService = ReportServiceImpl()
    val eventService = EventStoreImpl(reportService)
    val turnstile = TurnstileImpl()
    turnstile.bind(eventService)
    val managerAdmin = ManagerAdminImpl()
    managerAdmin.bind(eventService)
    var x: String? = readLine()
    while (x != null) {
        val command = x.split(" ")
        if (command[0] == "t") {
            if (command[1] == "in") {
                turnstile.enter(command[2].toLong(), true)
            } else {
                turnstile.enter(command[2].toLong(), false)
            }
        }
        if (command[0] == "m") {
            if (command[1] == "add") {
                println(managerAdmin.newUser())
            } else {
                managerAdmin.update(command[2].toLong(), 1L * 24 * 60 * 60 * 1000)
            }
        }
        x = readLine()
    }
}