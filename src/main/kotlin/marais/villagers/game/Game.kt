package marais.villagers.game

class Game(val map: GameMap, init: GameBuilder.() -> Unit) {

    val actors = mutableMapOf<String, Actor>()
    var step: Long = 500

    init {
        init(GameBuilder())
    }

    inner class GameBuilder {
        fun actor(actor: Actor) {
            this@Game.actors[actor.name] = actor
        }

        fun step(step: Long) {
            this@Game.step = step
        }
    }
}
