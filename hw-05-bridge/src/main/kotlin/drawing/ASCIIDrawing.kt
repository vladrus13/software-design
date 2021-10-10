package drawing

import java.nio.file.Path
import kotlin.io.path.bufferedWriter

class ASCIIDrawing : DrawingApi {

    private val width = 100
    private val height = 100

    private val gr = '■'
    private val empty = ' '

    private val picture = MutableList(height) { MutableList(width) { empty } }

    override fun getDrawingAreaWidth(): Long = width.toLong()

    override fun getDrawingAreaHeight(): Long = height.toLong()

    private fun fillSpace(point: Point, symbol: Char) {
        if (picture[point.y][point.x] == empty) {
            picture[point.y][point.x] = symbol
        }
    }

    override fun drawCircle(point: Point, number: Int) {
        check(number < 10)
        fillSpace(point, '0' + number)
    }

    override fun drawLine(start: Point, finish: Point) {
        val vector = finish - start
        val xCompleter = vector.y.toDouble() / vector.x
        val yCompleter = vector.x.toDouble() / vector.y
        for (x in (1 until vector.x)) {
            val additionalPoint = Point(x, (xCompleter * x).toInt())
            fillSpace(start + additionalPoint, gr)
        }
        for (y in (1 until vector.y)) {
            val additionalPoint = Point((yCompleter * y).toInt(), y)
            fillSpace(start + additionalPoint, gr)
        }
    }

    override fun save(path: Path) {
        path.resolve("graph.txt").bufferedWriter().use { bw ->
            picture.forEach { list ->
                bw.write(list.joinToString(separator = "") {
                    "" + it
                })
                bw.newLine()
            }
        }
    }
}