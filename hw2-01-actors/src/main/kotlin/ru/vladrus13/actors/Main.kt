package ru.vladrus13.actors

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import ru.vladrus13.actors.actor.RootSupervise

fun main() {
    val system = ActorSystem.create("Search")
    val root = system.actorOf(Props.create(RootSupervise::class.java), "root")
    root.tell("start", ActorRef.noSender())
}