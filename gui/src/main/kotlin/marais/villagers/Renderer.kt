package marais.villagers

import marais.villagers.game.Game
import marais.villagers.game.Position
import marais.villagers.game.Tile
import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class Renderer(val game: Game) : Canvas() {
    init {
        val handler = object : MouseAdapter() {

            var button = 0
            var previousPos: Position? = null

            override fun mousePressed(e: MouseEvent) {
                button = e.button
            }

            override fun mouseClicked(e: MouseEvent) {
                mouseDragged(e)
            }

            override fun mouseDragged(e: MouseEvent) {
                val pos = Position((e.x / width.toFloat() * game.map.width).toInt(), (e.y / height.toFloat() * game.map.height).toInt())
                if (pos in game.map && (previousPos == null || previousPos != pos)) {
                    previousPos = pos

                    if (button == MouseEvent.BUTTON1)
                        game.map[pos] = Tile.WALL
                    else if (button == MouseEvent.BUTTON3)
                        game.map[pos] = Tile.EMPTY

                    game.recalcPath()
                    repaint()
                }
            }

            override fun mouseReleased(e: MouseEvent?) {
                previousPos = null
            }
        }
        addMouseListener(handler)
        addMouseMotionListener(handler)

        addKeyListener(object : KeyAdapter() {
            override fun keyTyped(e: KeyEvent) {
                if (e.keyChar == 'a') {
                    game.recalcPath()
                    repaint()
                }
            }
        })
    }

    /**
     * Fuck awt
     */
    override fun update(g: Graphics?) {
        paint(g)
    }

    override fun paint(g: Graphics?) {
        if (bufferStrategy == null)
            createBufferStrategy(2, BufferCapabilities(ImageCapabilities(true), ImageCapabilities(true), BufferCapabilities.FlipContents.BACKGROUND))
        val g2d = bufferStrategy.drawGraphics as Graphics2D
        //val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

        //g2d.clearRect(0, 0, width, height)

        val cellWidth = width / game.map.width
        val cellHeight = height / game.map.height

        g2d.color = Color.BLACK
        for (x in 0 until game.map.width) {
            for (y in 0 until game.map.height) {
                //g2d.color = if (game.map[Position(x, y)] == Tile.EMPTY) Color.WHITE else Color.BLACK
                if (game.map[Position(x, y)] == Tile.WALL)
                    g2d.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight)
                else
                    g2d.drawRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight)
            }
        }

        g2d.color = Color.BLUE
        val (x, y) = game.playerPos
        g2d.fillRect(x * cellWidth, y * cellHeight, cellWidth, cellHeight)

        g2d.color = Color.RED
        val (i, j) = game.targetPos
        g2d.fillRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight)

        g2d.color = Color.GREEN
        // Maybe we should do a polyline with AOS to SOA
        game.currPath.flatMap { sequenceOf(it, it) }.drop(1).dropLast(1).zipWithNext().forEach {
            g2d.drawLine(((it.first.x + 0.5) * cellWidth).toInt(),
                    ((it.first.y + 0.5) * cellHeight).toInt(),
                    ((it.second.x + 0.5) * cellWidth).toInt(),
                    ((it.second.y + 0.5) * cellHeight).toInt())
        }

        g2d.dispose()
        bufferStrategy.show()
    }
}