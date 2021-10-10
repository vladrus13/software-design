package drawing

data class Point(val x : Int, val y : Int) {
    operator fun plus(another : Point) : Point {
        return Point(x + another.x, y + another.y)
    }

    operator fun minus(another : Point) : Point {
        return Point(x - another.x, y - another.y)
    }
}