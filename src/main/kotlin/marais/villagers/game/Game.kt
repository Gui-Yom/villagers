package marais.villagers.game

import marais.villagers.game.pathfinding.Astar
import marais.villagers.game.pathfinding.DefaultHeuristic

class Game(val map: GameMap) {
    val playerPos = Position(0, 0)
    val targetPos = Position(50, 50)
    val allowDiagonal = false
    val pathfinder = Astar(map, DefaultHeuristic, allowDiagonal)

    var currPath = pathfinder.find_path(playerPos, targetPos)

    fun recalcPath() {
        currPath = pathfinder.find_path(playerPos, targetPos)
    }
}
