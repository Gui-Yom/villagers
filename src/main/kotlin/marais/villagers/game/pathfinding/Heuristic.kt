package marais.villagers.game.pathfinding

import marais.villagers.game.GameMap
import marais.villagers.game.Position
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

interface Heuristic {
    var scale: Float
    operator fun invoke(map: GameMap, node: Position, goal: Position): Float
}

class Manhattan(override var scale: Float) : Heuristic {
    override operator fun invoke(map: GameMap, node: Position, goal: Position): Float =
        (abs(node.x - goal.x) + abs(node.y - goal.y)) * scale
}

class Euclidean(override var scale: Float) : Heuristic {
    override operator fun invoke(map: GameMap, node: Position, goal: Position): Float {
        val dx = abs(node.x - goal.x)
        val dy = abs(node.y - goal.y)
        return (sqrt((dx * dx + dy * dy).toDouble()) * scale).toFloat()
    }
}

open class Diagonal(override var scale: Float, var D2: Float) : Heuristic {
    override fun invoke(map: GameMap, node: Position, goal: Position): Float {
        val dx = abs(node.x - goal.x)
        val dy = abs(node.y - goal.y)
        return scale * max(dx, dy) + (D2 - scale) * min(dx, dy)
    }
}

class Chebyshev(scale: Float) : Diagonal(scale, 1f)

class Octile(scale: Float) : Diagonal(scale, sqrt(2f))

typealias DefaultHeuristic = Manhattan

val Heuristics = arrayOf(Manhattan(1f), Euclidean(1f), Chebyshev(1f), Octile(1f))
