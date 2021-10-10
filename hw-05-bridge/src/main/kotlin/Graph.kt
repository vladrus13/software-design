abstract class Graph(drawingApi: DrawingApi) {
    /**
     * Bridge to drawing api
     */
    private val drawingApi: DrawingApi
    abstract fun drawGraph()

    init {
        this.drawingApi = drawingApi
    }
}