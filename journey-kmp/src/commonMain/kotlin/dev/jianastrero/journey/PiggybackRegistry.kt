package dev.jianastrero.journey

import androidx.compose.runtime.compositionLocalOf

fun interface PiggybackHandler {
    suspend fun fire(stepId: String, journeyId: String)
}

class PiggybackRegistry {
    private val handlers = mutableMapOf<String, PiggybackHandler>()

    fun register(id: String, handler: PiggybackHandler) {
        handlers[id] = handler
    }

    fun register(id: String, block: suspend (stepId: String, journeyId: String) -> Unit) {
        handlers[id] = PiggybackHandler { stepId, journeyId -> block(stepId, journeyId) }
    }

    suspend fun fire(id: String, stepId: String = "", journeyId: String = "") {
        val handler = handlers[id]
        if (handler == null) {
            println("JourneyKMP Warning: no handler registered for piggyback id='$id' (step='$stepId', journey='$journeyId'). Call register(\"$id\") { … } before this step is entered.")
            return
        }
        handler.fire(stepId, journeyId)
    }
}

val LocalPiggybackRegistry = compositionLocalOf<PiggybackRegistry?> { null }
