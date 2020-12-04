package marais.villagers.game.pathfinding

import marais.villagers.game.GameMap
import marais.villagers.game.Position
import java.util.*

class Astar(
    val map: GameMap,
    var heuristic: Heuristic,
    var allowDiagonal: Boolean,
    positionA: () -> Position,
    goalA: () -> Position
) : Pathfinder(positionA, goalA) {

    var instance: Instance? = null

    override fun step() {
        if (found) return

        val start = positionA()
        val goal = goalA()

        if (start == goal) {
            path = emptyList()
            return
        }

        if (firstRun) {
            instance = Instance(start, goal)
            firstRun = false
        }
        instance!!.step()
    }

    /*
    override fun restart() {
        super.restart()
        instance = null
    }
     */

    /**
     * This class is a constant of start, goal, map, heuristic, allowDiagonal
     */
    inner class Instance(val start: Position, val goal: Position) {

        var current: Position? = null

        // We sort positions based on the cost
        var frontier = PriorityQueue(8, Comparator.comparing(Pair<Position, Float>::second))

        init {
            // The starting point is the first we should explore
            frontier.add(start to 0f)
        }

        // The explored map costs
        var costs = mutableMapOf(start to 0f)

        // TODO use bitset/bitvector
        // visited nodes
        var visited = mutableSetOf<Position>()

        // the path as a series of edges in reverse order
        var parent = mutableMapOf<Position, Position>()

        fun step() {
            if (frontier.isNotEmpty()) {
                current = frontier.poll().first

                if (current in visited) {
                    // This isn't really a step if we just skip, so we run another round in this case
                    step()
                    return
                }

                // Finish line
                if (current == goal) {
                    path = edgeToNodes()
                    return
                }

                visited.add(current!!)

                for (node in map.neighbors(current!!, allowDiagonal)) {
                    val newCost = costs[current]!! + map.cost(current!!, node)
                    if (node !in costs || newCost < costs[node]!!) {
                        costs[node] = newCost
                        frontier.add(node to (newCost + heuristic(map, node, goal!!)))
                        parent[node] = current!!
                    }
                }
            } else if (current != goal) {
                path = emptyList()
            }
        }

        /**
         * Convert the series of edge to a list of nodes and reverse the path in the process.
         */
        private fun edgeToNodes(): List<Position> {
            val path = mutableListOf(goal)
            while (path.last() != start) {
                path.add(parent[path.last()]!!)
            }
            return path.asReversed()
        }
    }
}
