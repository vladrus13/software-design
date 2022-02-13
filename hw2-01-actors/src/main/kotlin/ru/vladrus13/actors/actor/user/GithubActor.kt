package ru.vladrus13.actors.actor.user

import ru.vladrus13.actors.actor.AbstractGetter
import ru.vladrus13.actors.api.Getter
import ru.vladrus13.actors.api.user.Github

class GithubActor : AbstractGetter() {
    override val getter: Getter<*> = Github
    override val id: String = "Github Users"
}