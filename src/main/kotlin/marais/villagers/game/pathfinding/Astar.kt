package marais.villagers.game.pathfinding

import marais.villagers.game.GameMap
import marais.villagers.game.Position
import java.util.*

class Astar(val map: GameMap, val heuristic: Heuristic, val allowDiagonal: Boolean) : Pathfinder {
    override fun find_path(start: Position, target: Position): List<Position> {

        if (start == target)
            return listOf()

        // We sort positions based on the cost
        val frontier = PriorityQueue(Comparator.comparing(Pair<Position, Float>::second))
        frontier.add(start to 0f)
        val cost = mutableMapOf(start to 0f)
        val visited = mutableSetOf<Position>()

        val parent = mutableMapOf<Position, Position>()

        var current: Position? = null
        while (frontier.isNotEmpty()) {
            current = frontier.poll().first

            if (current in visited)
                continue

            visited.add(current)

            if (current == target)
                break

            /*
            println("visited: $visited")
            println("costs: $cost")
            println("frontier: $frontier")

             */

            for (node in map.neighbors(current, allowDiagonal)) {
                val newCost = cost[current]!! + map.cost(current, node)
                if (node !in cost || newCost < cost[node]!!) {
                    cost[node] = newCost
                    frontier.add(node to newCost + heuristic(node, target))
                    parent[node] = current
                }
            }
        }

        // No path
        if (current == null || current != target)
            return listOf()

        val path = mutableListOf(target)
        while (path.last() != start) {
            path.add(parent[path.last()]!!)
        }

        return path.asReversed()
    }
}
