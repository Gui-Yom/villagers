package marais.villagers.game.pathfinding

import marais.villagers.game.Position
import kotlin.math.abs
import kotlin.math.sqrt

typealias Heuristic = (pos1: Position, pos2: Position) -> Float

object Manhattan : Heuristic {
    override fun invoke(pos1: Position, pos2: Position): Float = (abs(pos1.x - pos2.x) + abs(pos1.y - pos2.y)).toFloat()
}

object Euclidean : Heuristic {
    override fun invoke(pos1: Position, pos2: Position): Float = sqrt(((pos2.x - pos1.x) * (pos2.x - pos1.x) + (pos2.y - pos1.y) * (pos2.y - pos1.y)).toDouble()).toFloat()
}

typealias DefaultHeuristic = Manhattan
