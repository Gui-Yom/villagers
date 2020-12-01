package marais.villagers.game.pathfinding

import marais.villagers.game.Position
import kotlin.math.abs

typealias Heuristic = (pos1: Position, pos2: Position) -> Int

object ManhattanHeuristic : Heuristic {
    override fun invoke(pos1: Position, pos2: Position): Int = abs(pos1.x - pos2.x) + abs(pos1.y - pos2.y)
}

typealias DefaultHeuristic = ManhattanHeuristic
