package dev.jianastrero.journey.example

import dev.jianastrero.journey.JourneyStep
import dev.jianastrero.journey.annotations.Exit
import dev.jianastrero.journey.annotations.Journey
import dev.jianastrero.journey.annotations.Piggyback
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_ENTER
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_EXIT
import dev.jianastrero.journey.annotations.Step

// Demonstrates two exit paths from a single step: password flow and SSO flow.
@Journey
sealed interface SignIn : JourneyStep {

    // ON_ENTER: fire immediately so the funnel registers even if the user bounces.
    // ON_EXIT: fires on departure regardless of direction — measures users who open
    // the auth screen but never enter credentials (bounce rate).
    @Step
    @Piggyback("analytics:screen_view", on = ON_ENTER)
    @Piggyback("analytics:credentials_screen_exit", on = ON_EXIT)
    @Exit("toPassword", EnterPassword::class)
    @Exit("toSSO", SSOLoading::class)
    data object EnterCredentials : SignIn

    // ON_EXIT for both: the auth method is confirmed when the user *leaves*,
    // and the audit log must capture the attempt whether they succeed or go back.
    @Step
    @Piggyback("analytics:auth_method_password", on = ON_EXIT)
    @Piggyback("log:password_auth_attempt", on = ON_EXIT)
    @Exit("toDone", Done::class)
    data class EnterPassword(val username: String) : SignIn

    // SSO provider is known on arrival — ON_ENTER is correct here.
    @Step
    @Piggyback("analytics:auth_method_sso", on = ON_ENTER)
    @Exit("toDone", Done::class)
    data class SSOLoading(val provider: String) : SignIn

    // Terminal step — KSP generates finish() on SignInDoneController.
    // session:start fires on arrival; cache prefetch fires on exit (just before navigating away).
    @Step
    @Piggyback("analytics:signin_completed", on = ON_ENTER)
    @Piggyback("session:start", on = ON_ENTER)
    @Piggyback("cache:prefetch_user_data", on = ON_EXIT)
    data object Done : SignIn
}
