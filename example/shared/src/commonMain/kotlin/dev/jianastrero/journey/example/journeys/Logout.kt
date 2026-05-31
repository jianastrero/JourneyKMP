package dev.jianastrero.journey.example

import dev.jianastrero.journey.JourneyStep
import dev.jianastrero.journey.annotations.Exit
import dev.jianastrero.journey.annotations.Journey
import dev.jianastrero.journey.annotations.Step

@Journey
sealed interface Logout : JourneyStep {

    @Step
    @Exit("toDone", Done::class)
    data object Confirm : Logout

    @Step
    data object Done : Logout
}
