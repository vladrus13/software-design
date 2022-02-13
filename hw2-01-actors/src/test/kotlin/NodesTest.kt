import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.UntypedAbstractActor
import org.junit.jupiter.api.Test
import ru.vladrus13.actors.actor.AbstractGetter
import ru.vladrus13.actors.actor.AbstractSupervise
import ru.vladrus13.actors.api.Getter
import java.lang.Thread.sleep
import java.util.concurrent.atomic.AtomicReference
import kotlin.reflect.KClass
import kotlin.test.assertEquals

class NodesTest {

    sealed class Tree(val id: String, val timeout: Long) {
        class Leaf(timeout: Long, id: String) : Tree(id, timeout)
        class Node(id: String, val list: List<Tree>, timeout: Long) : Tree(id, timeout)
    }

    class OneGetter(leaf: Tree.Leaf) : AbstractGetter() {

        class OneGetter : Getter<Int> {
            override fun getResult(query: String): Int = 1
        }

        override val getter: Getter<*> = OneGetter().apply {
            sleep(leaf.timeout)
        }

        override val id: String = leaf.id
    }

    class Supervise(node: Tree.Node) : AbstractSupervise<Int, Int>() {
        override val id: String = node.id
        override val classes: Map<String, Pair<KClass<out UntypedAbstractActor>, List<Any>>> = run {
            node.list.associate {
                it.id to Pair(if (it is Tree.Node) Supervise::class else OneGetter::class, listOf(it))
            }
        }
        override val timeout: Long = node.timeout
        override val aggregator: (List<Pair<String, Int?>>) -> Int = { it.mapNotNull { it.second }.sum() }
        override val code: String = "start"
    }

    data class Layer(val onLaunch: (UntypedAbstractActor) -> Unit)

    class TestActor<T>(val box: AtomicReference<T?>) : UntypedAbstractActor() {

        override fun onReceive(message: Any?) {
            if (message is Layer) {
                message.onLaunch(this)
            } else {
                box.set((message as AbstractSupervise.AbstractAnswer<T>).answer)
            }
        }
    }

    private fun <T> abstractTest(idOfTest: String, expected: T, layer: Layer) {
        val system = ActorSystem.create(idOfTest)
        val box: AtomicReference<T?> = AtomicReference(null)
        val testActor = system.actorOf(Props.create(TestActor::class.java, box), "testActor")
        testActor.tell(layer, ActorRef.noSender())
        while (true) {
            val underBox = box.get()
            if (underBox != null) {
                assertEquals(expected, underBox)
                return
            }
        }
    }

    @Test
    fun testEmptyActor() = abstractTest("testEmptyActor", 1, Layer {
        val child = it.context.actorOf(Props.create(OneGetter::class.java, Tree.Leaf(1000, "1")))
        child.tell("message", it.self())
    })

    @Test
    fun testOneSupervise() = abstractTest("testOneSupervise", 1, Layer {
        val child = it.context.actorOf(
            Props.create(
                Supervise::class.java,
                Tree.Node(
                    "super", listOf(
                        Tree.Leaf(3000, "1")
                    ), 10000L
                )
            )
        )
        child.tell("start", it.self())
    })

    private fun getBigTree(id: Int, depth: Int, branch: Int): Tree =
        if (depth == 0) Tree.Leaf(1000L * id, "$id")
        else Tree.Node("$id", (1..branch).map { getBigTree(id * branch + it, depth - 1, branch) }, 100000000L)

    @Test
    fun testBigTreeSupervise() = abstractTest("testBitTree", 2 * 2 * 2, Layer {
        val child = it.context.actorOf(
            Props.create(
                Supervise::class.java,
                getBigTree(0, 3, 2)
            )
        )
        child.tell("start", it.self())
    })

    @Test
    fun testCutTreeSupervise() = abstractTest("testCut", 5, Layer {
        val child = it.context.actorOf(
            Props.create(
                Supervise::class.java,
                Tree.Node(
                    "super",
                    (1..13).map { Tree.Leaf(it * 1000L, "$it") },
                    5500L
                )
            )
        )
        child.tell("start", it.self())
    })
}