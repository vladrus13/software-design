package drawing

import java.nio.file.Path

interface DrawingApi {
    fun getDrawingAreaWidth(): Long
    fun getDrawingAreaHeight(): Long
    fun drawCircle(point: Point, number: Int)
    fun drawLine(start: Point, finish: Point)
    fun save(path: Path)
}