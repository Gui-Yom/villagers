package marais.villagers

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import marais.villagers.game.Game
import marais.villagers.game.Position
import marais.villagers.game.Tile
import marais.villagers.game.pathfinding.Heuristics
import java.awt.event.*

class Controller(private val game: Game, private val renderer: Renderer) {

    init {

        // the heuristic scale
        var scale = 1f
        // Step speed
        var speed: Long = 500

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
                val pos = Position(
                    (e.x / renderer.width.toFloat() * game.map.width).toInt(),
                    (e.y / renderer.height.toFloat() * game.map.height).toInt()
                )
                if (pos in game.map && (previousPos == null || previousPos != pos)) {
                    previousPos = pos

                    // Paint tiles
                    if (button == MouseEvent.BUTTON1)
                        if (e.isShiftDown)
                            game.map[pos] = Tile.HARD
                        else
                            game.map[pos] = Tile.WALL
                    else if (button == MouseEvent.BUTTON3)
                        game.map[pos] = Tile.EMPTY

                    GlobalScope.launch {
                        restartSim()
                    }
                }
            }

            override fun mouseReleased(e: MouseEvent) {
                previousPos = null
            }

            // Heuristic weight
            override fun mouseWheelMoved(e: MouseWheelEvent) {
                scale += 0.1f * -e.wheelRotation
                if (scale < 0.8f || scale > 4f)
                    scale = 0.8f
                game.pathfinder.heuristic.scale = scale
                restartSim()
            }
        }
        renderer.addMouseListener(handler)
        renderer.addMouseMotionListener(handler)
        renderer.addMouseWheelListener(handler)

        renderer.addKeyListener(object : KeyAdapter() {
            override fun keyTyped(e: KeyEvent) {
                when (e.keyChar) {
                    // Reset
                    'r' -> GlobalScope.launch {
                        restartSim()
                    }
                    // Diagonals
                    'd' -> GlobalScope.launch {
                        game.pathfinder.allowDiagonal = !game.pathfinder.allowDiagonal
                        restartSim()
                    }
                    // Heuristics
                    'e' -> GlobalScope.launch {
                        // wow
                        game.pathfinder.heuristic =
                            Heuristics[(Heuristics.indexOf(game.pathfinder.heuristic) + 1) % Heuristics.size]
                        scale = 1f
                        game.pathfinder.heuristic.scale = scale
                        restartSim()
                    }
                    // Speed
                    's' -> GlobalScope.launch {
                        speed += 100
                        if (speed < 100 || speed > 1000)
                            speed = 100
                        game.step = speed
                        renderer.repaint()
                    }
                }
            }
        })
    }

    fun update(reason: UpdateReason) {
        when (reason) {
            UpdateReason.RESTART -> {
                game.pathfinder.restart()
                update(UpdateReason.REPAINT)
            }
            UpdateReason.REPAINT -> {
                renderer.repaint()
            }
        }
    }
}

enum class UpdateReason {
    RESTART,
    REPAINT
}
