package marais.villagers.game.pathfinding

import marais.villagers.game.Position

/**
 * Step by step pathfinder to plan a path from start to target.
 * We need it to maintain a state since we want to be able to step.
 *
 */
abstract class Pathfinder(protected val positionA: () -> Position, protected val goalA: () -> Position) {

    /**
     * the path if found or null
     */
    var path: List<Position>? = null

    var firstRun = true

    /**
     * Has a path been found ?
     */
    val found: Boolean
        get() = path != null

    /**
     * Runs a search step
     */
    abstract fun step()

    /**
     * Makes the pathfinder restart from the start.
     */
    open fun reset() {
        firstRun = true
        path = null
    }

    /**
     * @return the whole path in one go
     */
    fun findPath(): List<Position> {
        while (!found)
            step()
        return path!!
    }
}
