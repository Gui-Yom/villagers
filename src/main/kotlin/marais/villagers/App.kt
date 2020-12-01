package marais.villagers

import marais.villagers.game.Game
import marais.villagers.game.GameMap
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Frame
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import kotlin.system.exitProcess

class App {

    val frame = Frame("Villagers")
    val renderer = Renderer(Game(GameMap(16, 16)))

    suspend fun run() {
        frame.size = Dimension(400, 300)
        frame.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                // Shutdown gracefully
                exitProcess(0)
            }
        })
        frame.layout = BorderLayout()
        frame.add(renderer, BorderLayout.CENTER)
        frame.isVisible = true

        /*
        coroutineScope {
            launch(Dispatchers.Main) {
                println(Thread.currentThread().name)
            }
        }
        */
    }
}
