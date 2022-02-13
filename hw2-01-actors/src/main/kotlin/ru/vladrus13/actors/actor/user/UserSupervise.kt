package ru.vladrus13.actors.actor.user

import akka.actor.UntypedAbstractActor
import ru.vladrus13.actors.actor.AbstractSupervise
import kotlin.reflect.KClass

class UserSupervise : AbstractSupervise<Boolean, String>() {
    override val id: String = "User supervise"
    override val classes: Map<String, Pair<KClass<out UntypedAbstractActor>, List<Any>>> =
        mutableMapOf(
            "Github Users" to Pair(GithubActor::class, listOf()),
            "Gitlab Users" to Pair(GitlabActor::class, listOf()),
            "Stack Exchange User" to Pair(StackExchangeActor::class, listOf())
        )
    override val timeout: Long = 50000L
    override val aggregator: (List<Pair<String, Boolean?>>) -> String =
        { it.joinToString(separator = "\n") { "${it.first}: ${it.second}" } }
    override val code: String = "start"
}