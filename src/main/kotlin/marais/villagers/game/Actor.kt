package marais.villagers.game

import kotlinx.coroutines.CoroutineScope
import marais.villagers.game.pathfinding.Pathfinder

class Actor(
    scope: CoroutineScope,
    val name: String,
    var position: Position,
    var pathfinder: Pathfinder
) : BaseActor<Actor>(scope) {

    suspend fun getPosition() = executeInActor { it.position }

    suspend fun findPath(goal: Position) = executeInActor {
        it.pathfinder.goal = goal
        it.pathfinder.findPath()
    }

    suspend fun stepPath(goal: Position) = executeInActor {
        if (goal != it.pathfinder.goal) {
            it.pathfinder.reset()
        } else {

        }
    }
}
