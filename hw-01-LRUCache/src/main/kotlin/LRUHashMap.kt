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
    fun add(key: T, value: U) {
        check(map.size <= limit) {"Map limit was exceeded"}
        check(list.size <= limit) {"List limit was exceeded"}
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
        check(map.containsKey(key)) {"Key wasn't added"}
        check(map.size <= limit) {"Map limit was exceeded"}
        check(list.size <= limit) {"List limit was exceeded"}
    }

    /**
     * Get key from cache
     *
     * @param key key. PRE: key != null
     * @return value of key. If Key hasn't at cache, return null
     */
    fun get(key: T): U? {
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