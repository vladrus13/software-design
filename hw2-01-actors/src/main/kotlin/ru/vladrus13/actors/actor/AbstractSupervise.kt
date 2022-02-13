package ru.vladrus13.actors.actor

import akka.actor.*
import akka.japi.pf.DeciderBuilder
import java.time.Duration
import kotlin.reflect.KClass

abstract class AbstractSupervise<GET : Any, SEND : Any> : UntypedAbstractActor() {

    abstract val id: String
    abstract val classes: Map<String, Pair<KClass<out UntypedAbstractActor>, List<Any>>>
    abstract val timeout: Long
    abstract val aggregator: (List<Pair<String, GET?>>) -> SEND
    abstract val code: String

    var start: Long = 0L

    var answers: MutableMap<String, GET?>? = null

    data class AbstractAnswer<T>(val answer: T, val id: String)

    override fun supervisorStrategy(): SupervisorStrategy =
        OneForOneStrategy(
            false,
            DeciderBuilder.match(Exception::class.java) { OneForOneStrategy.stop() }.build()
        )

    private fun updateTimeout() {
        context.receiveTimeout = Duration.ofMillis(timeout - (System.currentTimeMillis() - start))
    }

    open fun result(answer: SEND) {
        context.parent.tell(AbstractAnswer(answer, id), self())
    }

    override fun onReceive(message: Any?) {
        when (message) {
            is ReceiveTimeout -> {
                result(aggregator(answers!!.toList()))
            }
            is AbstractAnswer<*> -> {
                val answer = message as AbstractAnswer<GET>
                context.stop(context.sender)
                answers!![answer.id] = message.answer
                if (answers!!.values.all { it != null }) {
                    result(aggregator(answers!!.toList()))
                }
                updateTimeout()
            }
            is String -> {
                answers = mutableMapOf()
                classes.forEach { (key, _) -> answers?.set(key, null) }
                start = System.currentTimeMillis()
                for (clazz in classes.values) {
                    val child = context.actorOf(Props.create(clazz.first.java, *(clazz.second.toTypedArray())))
                    child.tell(message, self())
                }
                updateTimeout()
            }
            else -> throw IllegalStateException("Can't get wrong type of message")
        }
    }

}