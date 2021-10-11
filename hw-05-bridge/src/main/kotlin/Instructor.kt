import drawing.ASCIIDrawing
import drawing.DrawingApi
import drawing.ImageDrawing
import graph.EdgeGraph
import graph.Graph
import graph.MatrixGraph
import java.nio.file.Files
import java.nio.file.Path

class Instructor {
    companion object {
        private val drawings: Map<String, () -> DrawingApi> = hashMapOf(
            Pair("ASCII") { ASCIIDrawing() },
            Pair("Image") { ImageDrawing() },
        )

        private val graphs: Map<String, (Path, DrawingApi) -> Graph> = hashMapOf(
            Pair("Matrix") { path, api -> MatrixGraph(api, path) },
            Pair("Edge") { path, api -> EdgeGraph(api, path) }
        )

        private val files: Map<String, Path> = hashMapOf(
            Pair("Matrix", Path.of("graphs").resolve("matrix.txt")),
            Pair("Edge", Path.of("graphs").resolve("edges.txt"))
        )

        fun adapt(filePath: Path? = null, path: Path, drawing: String, graph: String) {
            val realFilePath = filePath ?: files[graph]!!
            val drawingInstance = drawings[drawing]!!
            val graphInstance = graphs[graph]!!
            graphInstance.invoke(realFilePath, drawingInstance.invoke()).drawGraph(path)
        }

        fun allRun() {
            for (d in drawings.keys) {
                for (g in graphs.keys) {
                    val path = Path.of("").resolve(d).resolve(g)
                    Files.createDirectories(path)
                    adapt(null, path, d, g)
                }
            }
        }
    }
}