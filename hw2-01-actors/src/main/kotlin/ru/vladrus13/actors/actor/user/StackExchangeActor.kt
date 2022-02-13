package ru.vladrus13.actors.actor.user

import ru.vladrus13.actors.actor.AbstractGetter
import ru.vladrus13.actors.api.Getter
import ru.vladrus13.actors.api.user.StackExchange

class StackExchangeActor : AbstractGetter() {
    override val getter: Getter<*> = StackExchange
    override val id: String = "Stack Exchange User"
}