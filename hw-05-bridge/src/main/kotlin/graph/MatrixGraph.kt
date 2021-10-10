package graph

import drawing.DrawingApi
import java.nio.file.Path
import kotlin.io.path.readLines

class MatrixGraph(private val drawingApi: DrawingApi, path: Path) : CircleGraph(drawingApi) {

    private val matrix = path.readLines().map { line ->
        line.split("\\s+".toRegex()).map {
            it.toInt()
        }
    }

    init {
        matrix.forEach {
            check(it.size == matrix.size)
        }
    }

    override fun drawGraph(path: Path) {
        val listPoints = getCircle(matrix.size)
        matrix.indices.forEach {
            drawingApi.drawCircle(listPoints[it], it + 1)
        }
        matrix.forEachIndexed { i, list ->
            list.forEachIndexed { j, isEdge ->
                if (isEdge != 0) {
                    drawingApi.drawLine(listPoints[i], listPoints[j])
                }
            }
        }
        drawingApi.save(path)
    }
}