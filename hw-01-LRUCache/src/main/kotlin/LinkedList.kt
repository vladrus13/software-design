/**
 * Linked list
 *
 * @param T container type
 *
 * @see https://en.wikipedia.org/wiki/Linked_list#:~:text=In%20computer%20science%2C%20a%20linked,which%20together%20represent%20a%20sequence.
 */
class LinkedList<T> {
    /**
     * Node on linked list
     *
     * @param T container type
     * @property value value of list
     */
    class Node<T>(val value: T) {
        /**
         * Left node
         */
        var left: Node<T>? = null

        /**
         * Right node
         */
        var right: Node<T>? = null
    }

    /**
     * Leftest node
     */
    private var left: Node<T>? = null

    /**
     * Rightest node
     */
    private var right: Node<T>? = null

    /**
     * Size of list
     */
    var size = 0

    /**
     * Add value
     * PRE: value != null, size' = list.size
     * POST: list.size = size' + 1
     *
     * @param value value
     * @param function function to add node to list
     */
    private fun add(value: T?, function: (Node<T>) -> Unit) {
        check(value != null) { "Assertion add. Added value can't be null" }
        val newNode: Node<T> = Node(value)
        function(newNode)
        size++
        if (size == 1) {
            if (left == null) {
                left = right
            }
            if (right == null) {
                right = left
            }
        }
    }

    /**
     * Add node to left
     * PRE: value != null, size' = list.size
     * POST: list.size = size' + 1
     *
     * @param value value
     */
    fun addLeft(value: T?) {
        add(value) {
            it.right = left
            if (left != null) {
                left!!.left = it
            }
            left = it
        }
    }

    /**
     * Add node to right
     * PRE: value != null, size' = list.size
     * POST: list.size = size' + 1
     *
     * @param value value
     */
    fun addRight(value: T?) {
        add(value) {
            it.left = right
            if (right != null) {
                right!!.right = it
            }
            right = it
        }
    }

    /**
     * Remove one of end of list
     *
     * @param node node
     * @param function function to remove
     * @return removed item
     */
    private fun removeEnd(node: Node<T>?, function: () -> Unit): T {
        return when (size) {
            0 -> throw IllegalStateException("Assertion remove. Can't remove from empty list") // NoSuchElementException()
            1 -> {
                val x = right!!.value
                left = null
                right = null
                x
            }
            else -> {
                val x = node!!.value
                function()
                x
            }
        }
    }

    fun getLeft(): Node<T>? {
        return left
    }

    fun getRight(): Node<T>? {
        return right
    }

    fun getLeftValue(): T? {
        return left?.value
    }

    fun getRightValue(): T? {
        return right?.value
    }

    /**
     * Remove the rightest element
     * PRE: (size' = list.size) > 0
     * POST list.size = size' - 1
     *
     * @return rightest element
     */
    fun removeRight(): T {
        return removeEnd(right) { right = right!!.left }
    }

    /**
     * Remove the rightest element
     * PRE: (size' = list.size) > 0
     * POST list.size = size' - 1
     *
     * @return rightest element
     */
    fun removeLeft(): T {
        return removeEnd(left) { left = left!!.right }
    }

    /**
     * Remove element from list. If this element from another list, it will be removed from another list
     * PRE: (size' = list.size) > 0
     * POST: list.size = size' - 1
     *
     * @param value value
     * @return removed value
     */
    fun remove(value: Node<T>?): T {
        check(value != null) { "Assertion remove. Removed value can't be null" }
        val leftPart = value.left
        val rightPart = value.right
        if (leftPart != null) {
            leftPart.right = rightPart
        }
        if (rightPart != null) {
            rightPart.left = leftPart
        }
        value.left = null
        value.right = null
        return value.value
    }
}