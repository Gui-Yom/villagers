package marais.villagers.game

import marais.villagers.game.pathfinding.Astar
import marais.villagers.game.pathfinding.DefaultHeuristic

class Game(val map: GameMap) {
    val playerPos = Position(0, 0)
    val targetPos = Position(5, 0)
    val allowDiagonal = true
    val pathfinder = Astar(map, DefaultHeuristic, allowDiagonal)

    var currPath = pathfinder.find_path(playerPos, targetPos)

    fun recalc_path() {
        currPath = pathfinder.find_path(playerPos, targetPos)
    }
}
