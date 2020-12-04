package marais.villagers.game

data class Position(val x: Int, val y: Int) {

    operator fun plus(rhs: Position) = Position(x + rhs.x, y + rhs.y)

    operator fun plus(rhs: Pair<Int, Int>) = Position(x + rhs.first, y + rhs.second)
}
