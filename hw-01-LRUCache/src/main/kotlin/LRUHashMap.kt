/**
 * LRU Cache Structure
 *
 * @param T Key type
 * @param U Value type
 * @property limit limit of count elements in cache. PRE: limit > 0
 *
 * @see https://en.wikipedia.org/wiki/Cache_replacement_policies
 */
class LRUHashMap<T, U>(private val limit: Int) {

    init {
        check(limit > 0) { AssertionError("Limit of cache must be positive") }
    }

    private val map: HashMap<T, LinkedList.Node<Pair<T, U>>> = hashMapOf()
    private val list: LinkedList<Pair<T, U>> = LinkedList()
    private var size = 0

    /**
     * Add key to the cache
     *
     * @param key key. PRE: key != null
     * @param value value. PRE: value != null
     */
    fun add(key: T?, value: U?) {
        check(key != null) { "Assert add. Can't add with null key" }
        check(value != null) { "Assert add. Can't add with null value" }
        if (map.containsKey(key)) {
            val node = map[key]!!
            list.remove(node)
            list.addRight(node.value)
        } else {
            val pair = Pair(key, value)
            list.addRight(pair)
            map[key] = list.getRight()!!
            if (size == limit) {
                val node = list.getLeftValue()!!
                list.removeLeft()
                map.remove(node.first)
            } else {
                size++
            }
        }
    }

    /**
     * Get key from cache
     *
     * @param key key. PRE: key != null
     * @return value of key. If Key hasn't at cache, return null
     */
    fun get(key: T?): U? {
        check(key != null) { "Assert get. Can't get with null key" }
        return if (map.containsKey(key)) {
            val node = map[key]!!
            list.remove(node)
            list.addRight(node.value)
            node.value.second
        } else {
            null
        }
    }
}