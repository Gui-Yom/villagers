package marais.villagers.game

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.actor

/**
 * Provides a way of confining a state to a coroutine transparently.
 */
abstract class BaseActor<B : BaseActor<B>>(scope: CoroutineScope) {
    private val core = scope.actor<ActorMsg<Any>> {
        for (msg in channel) {
            try {
                msg.deferred.complete(msg.action(this@BaseActor as B))
            } catch (e: Exception) {
                msg.deferred.completeExceptionally(e)
            }
        }
    }

    suspend fun <T : Any> executeInActor(block: (B) -> T): T {
        val deferred = CompletableDeferred<T>()
        core.send(ActorMsg(deferred, block) as ActorMsg<Any>)
        return deferred.await()
    }

    private inner class ActorMsg<T>(val deferred: CompletableDeferred<T>, val action: (B) -> T)
}
