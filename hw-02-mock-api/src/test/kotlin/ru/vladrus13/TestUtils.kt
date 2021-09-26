package ru.vladrus13

class TestUtils {
    companion object {
        fun <T> assertList(a: List<T>, b: List<T>) {
            assert(a.size == b.size) { "Different lists: $a, $b" }
            (a.indices).forEach {
                assert(a[it] == b[it]) { "Different lists: $a, $b" }
            }
        }
    }
}