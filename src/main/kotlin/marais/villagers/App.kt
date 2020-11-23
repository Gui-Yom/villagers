package marais.villagers

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Frame
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import kotlin.system.exitProcess

class App {

    val frame = Frame("Villagers")

    suspend fun run() {
        frame.size = Dimension(800, 600)
        frame.addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                // Shutdown gracefully
                exitProcess(0)
            }
        })
        frame.layout = BorderLayout()
        frame.isVisible = true
    }
}
