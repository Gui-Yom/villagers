package marais.villagers

import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import marais.villagers.game.Actor
import marais.villagers.game.Game
import marais.villagers.game.GameMap
import marais.villagers.game.Position
import marais.villagers.game.pathfinding.Astar
import marais.villagers.game.pathfinding.Heuristics
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Frame
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import kotlin.system.exitProcess

object Main {

    init {
        // Required for hardware acceleration
        System.setProperty("sun.java2d.opengl", "true")
    }

    val map = GameMap(32, 32)
    val game = Game(map) {
        step(500)
        actor(Actor("Alice", Position(0, 0), Position(28, 28), Astar(Heu)))
    }

    // Visualization rendering
    val renderer = Renderer(game)

    // Visualization controls
    val controller = Controller(game, renderer)

    // The window
    val frame = Frame("Villagers")

    val stepMode = false

    suspend fun run() {
        frame.size = Dimension(800, 800)
        frame.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                // Shutdown gracefully
                exitProcess(0)
            }
        })
        frame.layout = BorderLayout()
        frame.add(renderer, BorderLayout.CENTER)
        frame.isVisible = true
        renderer.requestFocus()

        renderer.repaint()
    }
}

suspend fun main() {
    Main.run()
}
