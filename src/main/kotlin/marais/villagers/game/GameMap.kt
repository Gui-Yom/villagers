package marais.villagers.game

fun Pair<Int, Int>.toPosition() = Position(first, second)

data class Position(val x: Int, val y: Int) {
    fun toPair() = x to y

    operator fun plus(rhs: Position) = Position(x + rhs.x, y + rhs.y)

    operator fun plus(rhs: Pair<Int, Int>) = Position(x + rhs.first, y + rhs.second)
}

class GameMap(val width: Int, val height: Int) : Iterable<Position> {
    val inner = Array(width) { Array(height) { Tile.EMPTY } }

    operator fun get(pos: Position) = get(pos.x, pos.y)

    operator fun get(x: Int, y: Int): Tile = inner[x][y]

    operator fun set(pos: Position, tile: Tile) {
        inner[pos.x][pos.y] = tile
    }

    operator fun contains(pos: Position) = isInBounds(pos)

    fun isInBounds(pos: Position) = pos.x in 0 until width && pos.y in 0 until height

    fun isTraversable(pos: Position) = isInBounds(pos) && this[pos].isTraversable()

    fun neighbors(pos: Position, includeDiagonal: Boolean): List<Position> {
        return if (includeDiagonal) {
            arrayOf(
                pos + (-1 to -1), pos + (-1 to 0), pos + (-1 to 1),
                pos + (0 to -1), pos + (0 to 1),
                pos + (1 to -1), pos + (1 to 0), pos + (1 to 1)
            )
                .filter { isTraversable(it) }
        } else {
            arrayOf(
                pos + (-1 to 0), pos + (1 to 0),
                pos + (0 to -1), pos + (0 to 1)
            )
                .filter { isTraversable(it) }
        }
    }

    /**
     * pos1 & pos2 are adjacent
     *
     * @return the cost to go from pos1 to pos2
     */
    fun cost(pos1: Position, pos2: Position): Float = this[pos2].cost

    override fun iterator(): Iterator<Position> = Itr()

    inner class Itr : Iterator<Position> {

        // We could also use a modulo
        var i: Int = 0
        var j: Int = -1

        override fun hasNext(): Boolean = if (i < width - 1) true else j < height - 1

        override fun next(): Position {
            j += 1
            if (j >= height) {
                i += 1
                j = 0
            }
            val pos = Position(i, j)
            return pos
        }
    }
}

enum class Tile(val cost: Float) {
    EMPTY(1f),
    WALL(-1f),
    HARD(2f),
    VERYHARD(3f);

    fun isTraversable() = cost >= 0
}
