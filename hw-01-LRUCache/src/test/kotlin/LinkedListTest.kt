import org.junit.Test
import kotlin.test.assertEquals

internal class LinkedListTest {

    @Test
    fun testCreate() {
        LinkedList<Int>()
    }

    @Test
    fun testAddOneElement() {
        val linkedList = LinkedList<Int>()
        linkedList.addRight(1)
    }

    @Test(expected = IllegalStateException::class)
    fun testRemoveBadElement() {
        val linkedList = LinkedList<Int>()
         linkedList.removeLeft()
    }

    @Test
    fun testAddAndRemove() {
        val linkedList = LinkedList<Int>()
        linkedList.addRight(4)
        assertEquals(4, linkedList.removeLeft())
    }

    @Test
    fun testAdd3() {
        val linkedList = LinkedList<Int>()
        linkedList.addRight(1)
        linkedList.addRight(2)
        linkedList.addRight(3)
        assertEquals(3, linkedList.removeRight())
        assertEquals(1, linkedList.removeLeft())
        assertEquals(2, linkedList.removeLeft())
    }

    @Test
    fun testAdd100Right() {
        val linkedList = LinkedList<String>()
        (0..100).map { it.toString() }.forEach { linkedList.addRight(it) }
        (100 downTo 0).map { it.toString() }.forEach { assertEquals(it, linkedList.removeRight()) }
    }

    @Test
    fun testAdd100() {
        val linkedList = LinkedList<String>()
        (0..100).map { it.toString() }.forEach { linkedList.addRight(it) }
        (0..100).map { it.toString() }.forEach { assertEquals(it, linkedList.removeLeft()) }
    }

    @Test(expected = IllegalStateException::class)
    fun testAddNull() {
        val linkedList = LinkedList<String>()
        linkedList.addRight(null)
    }
}