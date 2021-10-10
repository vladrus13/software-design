package graph

import drawing.DrawingApi
import drawing.Point
import java.lang.Long.min
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

abstract class CircleGraph(private val drawingApi: DrawingApi) : Graph(drawingApi) {
    fun getCircle(n : Int) : List<Point> {
        val angle = PI * 2 / n
        val size = (min(drawingApi.getDrawingAreaHeight(), drawingApi.getDrawingAreaWidth()) - 10) / 2
        val start = Point(size.toInt(), size.toInt())
        return (0 until n).map { it * angle }
            .map { start + Point((-size * sin(it)).toInt(), (size * cos(it)).toInt()) }
    }
}