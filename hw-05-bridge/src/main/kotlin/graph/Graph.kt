package graph

import drawing.DrawingApi
import java.nio.file.Path

abstract class Graph(drawingApi: DrawingApi) {
    /**
     * Bridge to drawing api
     */
    private val drawingApi: DrawingApi
    abstract fun drawGraph(path: Path)

    init {
        this.drawingApi = drawingApi
    }
}