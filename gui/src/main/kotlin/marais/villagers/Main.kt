package marais.villagers

import kotlinx.coroutines.delay
import marais.villagers.game.Game
import marais.villagers.game.GameMap
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
    val game = Game(map, true, Heuristics[0])

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
        if (stepMode)
            while (true) {
                game.pathfinder.step()
                //println("current: ${game.pathfinder.current}")
                //println("frontier: ${game.pathfinder.frontier}")
                renderer.repaint()
                delay(game.step)
            }
        else {
            game.pathfinder.findPath()
        }
        renderer.repaint()
    }
}

suspend fun main() {
    Main.run()
}
