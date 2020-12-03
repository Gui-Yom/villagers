package marais.villagers.game.pathfinding

import marais.villagers.game.GameMap
import marais.villagers.game.Position
import java.util.*

// TODO thread safe because its currently the big oof when handling UI events
class Astar(
    private val map: GameMap,
    var heuristic: Heuristic,
    var allowDiagonal: Boolean,
    val start: Position,
    val target: Position
) : Pathfinder {

    override var path: List<Position>? = null

    init {
        // The Programmer
        if (start == target)
            path = emptyList()
    }

    var current: Position? = null

    // We sort positions based on the cost
    var frontier = PriorityQueue(Comparator.comparing(Pair<Position, Float>::second))

    // The explored map costs
    var costs = mutableMapOf(start to 0f)

    // TODO use bitset/bitvector
    // visited nodes
    var visited = mutableSetOf<Position>()

    // the path as a series of edges in reverse order
    var parent = mutableMapOf<Position, Position>()

    init {
        // The starting point is the first we should explore
        frontier.add(start to 0f)
    }

    override fun step() {
        if (found) return

        if (frontier.isNotEmpty()) {
            current = frontier.poll().first

            if (current in visited) {
                // This isn't really a step if we just skip, so we run another round in this case
                step()
                return
            }

            // Finish line
            if (current == target) {
                path = edgeToNodes()
                return
            }

            visited.add(current!!)

            for (node in map.neighbors(current!!, allowDiagonal)) {
                val newCost = costs[current]!! + map.cost(current!!, node)
                if (node !in costs || newCost < costs[node]!!) {
                    costs[node] = newCost
                    frontier.add(node to (newCost + heuristic(map, node, target)))
                    parent[node] = current!!
                }
            }
        } else if (current != target) {
            path = emptyList()
        }
    }

    override fun restartFrom(pos: Position) {
        TODO("Not yet implemented")
    }

    override fun restart() {
        path = null
        current = null
        // We sort positions based on the cost
        frontier = PriorityQueue(Comparator.comparing(Pair<Position, Float>::second))
        // The starting point is the first we should explore
        frontier.add(start to 0f)
        // The explored map costs
        costs = mutableMapOf(start to 0f)
        // visited nodes
        visited = mutableSetOf()
        // the path as a series of edges in reverse order
        parent = mutableMapOf()
    }

    override fun findPath(): List<Position> {
        while (!found)
            step()
        return path!!
    }

    /**
     * Convert the series of edge to a list of nodes and reverse the path in the process.
     */
    private fun edgeToNodes(): List<Position> {
        val path = mutableListOf(target)
        while (path.last() != start) {
            path.add(parent[path.last()]!!)
        }
        return path.asReversed()
    }
}
