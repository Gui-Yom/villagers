package marais.villagers

import kotlinx.coroutines.coroutineScope

suspend fun main() = coroutineScope {
    System.setProperty("kotlinx.coroutines.fast.service.loader", "false")
    App().run()
}
