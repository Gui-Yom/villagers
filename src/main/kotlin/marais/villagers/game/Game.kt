package marais.villagers.game

import marais.villagers.game.pathfinding.Astar
import marais.villagers.game.pathfinding.Heuristic

class Game(val map: GameMap, allowDiagonal: Boolean, h: Heuristic) {
    val playerPos = Position(0, 0)
    val targetPos = Position(28, 28)
    val pathfinder = Astar(map, h, allowDiagonal, playerPos, targetPos)
    var step: Long = 500
}
