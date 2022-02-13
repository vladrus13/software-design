package ru.vladrus13.actors.actor.user

import ru.vladrus13.actors.actor.AbstractGetter
import ru.vladrus13.actors.api.Getter
import ru.vladrus13.actors.api.user.Gitlabs

class GitlabActor : AbstractGetter() {
    override val getter: Getter<*> = Gitlabs
    override val id: String = "Gitlab Users"
}