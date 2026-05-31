package dev.jianastrero.journey.example

import dev.jianastrero.journey.JourneyStep
import dev.jianastrero.journey.annotations.Exit
import dev.jianastrero.journey.annotations.Journey
import dev.jianastrero.journey.annotations.Piggyback
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_ENTER
import dev.jianastrero.journey.annotations.Step

// Demonstrates two exit paths from a single step: password flow and SSO flow.
@Journey
sealed interface SignIn : JourneyStep {

    // Initial step — must be a data object. Offers two exits:
    // toPassword navigates to the password step; toSSO navigates to the SSO loading step.
    @Step
    @Exit("toPassword", EnterPassword::class)
    @Exit("toSSO", SSOLoading::class)
    data object EnterCredentials : SignIn

    @Step
    @Exit("toDone", Done::class)
    data class EnterPassword(val username: String) : SignIn

    // Fake SSO handshake — auto-advances to Done after a short delay.
    @Step
    @Exit("toDone", Done::class)
    data class SSOLoading(val provider: String) : SignIn

    // Terminal step — KSP generates finish() on SignInDoneController.
    @Step
    @Piggyback("analytics:signin_completed", on = ON_ENTER)
    data object Done : SignIn
}
