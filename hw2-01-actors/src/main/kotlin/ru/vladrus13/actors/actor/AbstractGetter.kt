package ru.vladrus13.actors.actor

import akka.actor.UntypedAbstractActor
import ru.vladrus13.actors.api.Getter

abstract class AbstractGetter : UntypedAbstractActor() {

    abstract val getter: Getter<*>
    abstract val id: String

    override fun onReceive(message: Any?) {
        if (message is String) {
            val answer = AbstractSupervise.AbstractAnswer(getter.getResult(message), id)
            context.parent.tell(answer, self())
            return
        }
        if (message != null) {
            throw IllegalArgumentException("Unknown type of argument: ${message::class}")
        }
    }

}