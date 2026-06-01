@file:Suppress("InvalidPackageDeclaration", "unused")

package dev.jianastrero.journey.example.journeys

import dev.jianastrero.journey.JourneyStep
import dev.jianastrero.journey.annotations.Exit
import dev.jianastrero.journey.annotations.Journey
import dev.jianastrero.journey.annotations.Piggyback
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_ENTER
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_EXIT
import dev.jianastrero.journey.annotations.Step

@Journey
sealed interface Logout : JourneyStep {

    // ON_ENTER: initiation captured immediately so cancellation (back button) is
    // distinguishable from completion in the analytics funnel.
    // ON_EXIT: logout decision made on departure — fires whether confirmed or cancelled,
    //          enabling measurement of the confirm rate.
    @Step
    @Piggyback("analytics:logout_initiated", on = ON_ENTER)
    @Piggyback("analytics:logout_decision", on = ON_EXIT)
    @Exit("toDone", Done::class)
    data object Confirm : Logout

    // ON_ENTER: immediate success signal — user sees the logout confirmation screen.
    // ON_EXIT: session teardown and cache wipe deferred until after UI has fully resolved.
    @Step
    @Piggyback("analytics:logout_success", on = ON_ENTER)
    @Piggyback("analytics:logout_completed", on = ON_EXIT)
    @Piggyback("session:end", on = ON_EXIT)
    @Piggyback("cache:clear_user_data", on = ON_EXIT)
    data object Done : Logout
}
