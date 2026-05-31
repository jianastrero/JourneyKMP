package dev.jianastrero.journey.example

import dev.jianastrero.journey.JourneyStep
import dev.jianastrero.journey.annotations.Exit
import dev.jianastrero.journey.annotations.Journey
import dev.jianastrero.journey.annotations.Piggyback
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_ENTER
import dev.jianastrero.journey.annotations.Step

// Three-step registration flow with an SSO shortcut from the first step.
@Journey
sealed interface SignUp : JourneyStep {

    // First step — data object. Offers toEnterEmail (standard flow) and toSSO (shortcut).
    @Step
    @Exit("toEnterEmail", EnterEmail::class)
    @Exit("toSSO", SSOLoading::class)
    data object EnterUsername : SignUp

    // Username is threaded forward so subsequent steps can display it.
    @Step
    @Exit("toEnterPassword", EnterPassword::class)
    data class EnterEmail(val username: String) : SignUp

    @Step
    @Exit("toDone", Done::class)
    data class EnterPassword(val username: String, val email: String) : SignUp

    @Step
    @Exit("toDone", Done::class)
    data class SSOLoading(val provider: String) : SignUp

    @Step
    @Piggyback("analytics:signup_completed", on = ON_ENTER)
    data object Done : SignUp
}
