package drawing

import java.awt.Graphics
import java.awt.image.BufferedImage
import java.nio.file.Path
import javax.imageio.ImageIO

class ImageDrawing : DrawingApi {

    private val width = 1000
    private val height = 1000
    private val circleRadius = 10

    private val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    private val graphics: Graphics = bufferedImage.graphics

    override fun getDrawingAreaWidth(): Long = width.toLong()

    override fun getDrawingAreaHeight(): Long = height.toLong()

    private fun makeNormal(point: Point) = Point(point.x, height - point.y)

    override fun drawCircle(point: Point, number: Int) {
        val normal = makeNormal(Point(point.x - circleRadius, point.y + circleRadius))
        graphics.drawOval(normal.x, normal.y, circleRadius * 2, circleRadius * 2)
        graphics.drawString(number.toString(), normal.x, normal.y)
    }

    override fun drawLine(start: Point, finish: Point) {
        val normalStart = makeNormal(start)
        val normalFinish = makeNormal(finish)
        graphics.drawLine(normalStart.x, normalStart.y, normalFinish.x, normalFinish.y)
    }

    override fun save(path: Path) {
        ImageIO.write(bufferedImage, "PNG", path.resolve("image.png").toFile())
    }
}