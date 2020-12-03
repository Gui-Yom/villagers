package marais.villagers.game.pathfinding

import marais.villagers.game.Position

/**
 * Step by step pathfinder to plan a path from start to target.
 */
interface Pathfinder {
    /**
     * Runs a search step
     */
    fun step()

    /**
     * @return the path if found or null
     */
    val path: List<Position>?

    /**
     * Has a path been found ?
     */
    val found: Boolean
        get() = path != null

    /**
     * Makes the pathfinder restart from a given position on the path
     */
    fun restartFrom(pos: Position)

    /**
     * Makes the pathfinder restart from the start.
     */
    fun restart()

    /**
     * @return the whole path in one go
     */
    fun findPath(): List<Position>
}

typealias DefaultPathfinder = Astar
