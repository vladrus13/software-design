package graph

import drawing.DrawingApi
import java.nio.file.Path
import kotlin.io.path.readLines

class EdgeGraph(private val drawingApi: DrawingApi, path: Path) : CircleGraph(drawingApi) {

    private val edges = path.readLines().map { list ->
        list.split("\\s+".toRegex()).map {
            it.toInt() - 1
        }
    }

    init {
        edges.forEach { list ->
            list.forEach {
                check(it < edges.size && it >= 0)
            }
        }
    }

    override fun drawGraph(path: Path) {
        val listPoints = getCircle(edges.size)
        edges.indices.forEach {
            drawingApi.drawCircle(listPoints[it], it + 1)
        }
        edges.forEachIndexed { index, list ->
            list.forEach {
                drawingApi.drawLine(listPoints[index], listPoints[it])
            }
        }
        drawingApi.save(path)
    }
}