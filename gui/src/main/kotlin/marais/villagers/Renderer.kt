package marais.villagers

import marais.villagers.game.Game
import marais.villagers.game.Position
import marais.villagers.game.Tile
import java.awt.*
import java.awt.event.*
import java.text.DecimalFormat

class Renderer(private val game: Game) : Canvas() {

    private val costFont = Font("Consolas", Font.PLAIN, 9)
    private val infoFont = costFont.deriveFont(Font.BOLD, 13f)

    init {
        background = Color.WHITE
    }

    /**
     * The stock canvas component clears the buffer using non accelerated graphics effectively
     * causing brief flashes at each redraw
     */
    override fun update(g: Graphics?) {
        paint(g)
    }

    override fun isDoubleBuffered(): Boolean = true

    override fun paint(g: Graphics?) {
        // ACCELERATE, FASTER FASTER FASTER
        if (bufferStrategy == null)
            createBufferStrategy(
                2,
                BufferCapabilities(
                    ImageCapabilities(true),
                    ImageCapabilities(true),
                    BufferCapabilities.FlipContents.BACKGROUND
                )
            )

        val startTime = System.currentTimeMillis()

        val g2d = bufferStrategy.drawGraphics as Graphics2D
        //val g2d = g as Graphics2D
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)

        // TODO use floating points and rounds to make the frame pixel perfect
        val cellWidth = width / game.map.width
        val cellHeight = height / game.map.height

        // Tile painting
        for (pos in game.map) {
            val (i, j) = pos.toPx(cellWidth, cellHeight)
            when {
                game.map[pos] == Tile.WALL -> {
                    g2d.color = Color.BLACK
                    g2d.fillRect(i, j, cellWidth, cellHeight)
                }
                pos == game.playerPos -> {
                    g2d.color = Color.BLUE
                    g2d.fillRect(i, j, cellWidth, cellHeight)
                }
                pos == game.targetPos -> {
                    g2d.color = Color.RED
                    g2d.fillRect(i, j, cellWidth, cellHeight)
                }
                pos in game.pathfinder.frontier.map(Pair<Position, Float>::first) -> {
                    g2d.color = Color.CYAN
                    g2d.fillRect(i, j, cellWidth, cellHeight)
                }
                pos in game.pathfinder.visited -> {
                    g2d.color = Color.GRAY
                    g2d.fillRect(i, j, cellWidth, cellHeight)
                }
                // Empty cell
                else -> {
                    g2d.color = Color.BLACK
                    g2d.drawRect(i, j, cellWidth, cellHeight)
                }
            }
        }

        g2d.color = Color.GREEN
        if (game.pathfinder.current != null) {
            val (a, b) = game.pathfinder.current!!.toPx(cellWidth, cellHeight)
            g2d.fillRect(a, b, cellWidth, cellHeight)
        }

        g2d.color = Color.BLACK
        g2d.font = costFont
        val format = DecimalFormat.getIntegerInstance()
        for ((pos, cost) in game.pathfinder.costs) {
            g2d.drawString(format.format(cost.toDouble()), (pos.x + 0.2f) * cellWidth, (pos.y + 0.8f) * cellHeight)
        }

        g2d.stroke = BasicStroke(3f)
        g2d.color = Color.GREEN
        // We should do the full path in one call (AOS to SOA)
        if (game.pathfinder.found)
            game.pathfinder.path!!
                .map { it.toPxCenter(cellWidth, cellHeight) }
                .flatMap { sequenceOf(it, it) }
                .drop(1).dropLast(1)
                .zipWithNext()
                .forEach {
                    g2d.drawLine(it.first.x, it.first.y, it.second.x, it.second.y)
                }

        g2d.color = Color.BLACK
        g2d.font = infoFont
        g2d.drawString(
            "heuristic: ${game.pathfinder.heuristic.javaClass.simpleName}; scale: ${game.pathfinder.heuristic.scale}; step: ${game.step}",
            5,
            height - 4
        )
        val time = System.currentTimeMillis() - startTime
        g2d.drawString(
            "$time ms",
            width - 60,
            height - 4
        )

        g2d.dispose()
        bufferStrategy.show()
    }

    private fun Position.toPx(cellWidth: Int, cellHeight: Int) = Position(x * cellWidth, y * cellHeight)

    private fun Position.toPxCenter(cellWidth: Int, cellHeight: Int) = Position(
        ((x + 0.5) * cellWidth).toInt(),
        ((y + 0.5) * cellHeight).toInt()
    )
}