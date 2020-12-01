package marais.villagers.game.pathfinding

import marais.villagers.game.Position

interface Pathfinder {
    fun find_path(pos: Position, target: Position): List<Position>
}
