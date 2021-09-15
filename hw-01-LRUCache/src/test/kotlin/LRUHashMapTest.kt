import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class LRUHashMapTest {
    @Test
    fun testAddOne() {
        val map = LRUHashMap<Int, String>(100)
        map.add(1, "1")
    }

    @Test
    fun testAddLimit() {
        val map = LRUHashMap<Int, String>(176)
        for (i in 0..175) {
            map.add(i, i.toString())
        }
        for (i in 175 downTo 0) {
            assertEquals(i.toString(), map.get(i))
        }
    }

    @Test
    fun testAddMoreLimit() {
        val map = LRUHashMap<Int, String>(10)
        for (i in 0..100) {
            map.add(i, i.toString())
        }
        for (i in 0..90) {
            assertNull(map.get(i))
        }
        for (i in 91..100) {
            assertEquals(i.toString(), map.get(i))
        }
    }
}