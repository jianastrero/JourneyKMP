package dev.jianastrero.journey.example

import dev.jianastrero.journey.JourneyStep
import dev.jianastrero.journey.annotations.Exit
import dev.jianastrero.journey.annotations.Journey
import dev.jianastrero.journey.annotations.Piggyback
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_ENTER
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_EXIT
import dev.jianastrero.journey.annotations.Step

// Three-step registration flow with an SSO shortcut from the first step.
@Journey
sealed interface SignUp : JourneyStep {

    // Funnel entry — ON_ENTER so drop-off at the very first screen is visible.
    @Step
    @Piggyback("analytics:signup_funnel_start", on = ON_ENTER)
    @Exit("toEnterEmail", EnterEmail::class)
    @Exit("toSSO", SSOLoading::class)
    data object EnterUsername : SignUp

    // ON_EXIT: the user *completed* step 1 when they leave this screen.
    // Username is threaded forward so subsequent steps can display it.
    @Step
    @Piggyback("analytics:signup_funnel_step2", on = ON_EXIT)
    @Exit("toEnterPassword", EnterPassword::class)
    data class EnterEmail(val username: String) : SignUp

    // ON_EXIT: step 2 is done on departure; step 3 begins on the next screen.
    @Step
    @Piggyback("analytics:signup_funnel_step3", on = ON_EXIT)
    @Exit("toDone", Done::class)
    data class EnterPassword(val username: String, val email: String) : SignUp

    @Step
    @Piggyback("analytics:auth_method_sso", on = ON_ENTER)
    @Exit("toDone", Done::class)
    data class SSOLoading(val provider: String) : SignUp

    // analytics + session on arrival; welcome notification deferred to exit
    // so it fires after the Done screen has fully rendered.
    @Step
    @Piggyback("analytics:signup_completed", on = ON_ENTER)
    @Piggyback("session:start", on = ON_ENTER)
    @Piggyback("notification:welcome", on = ON_EXIT)
    data object Done : SignUp
}
