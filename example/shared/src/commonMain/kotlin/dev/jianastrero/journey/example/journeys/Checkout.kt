package dev.jianastrero.journey.example

import dev.jianastrero.journey.JourneyStep
import dev.jianastrero.journey.annotations.Exit
import dev.jianastrero.journey.annotations.Journey
import dev.jianastrero.journey.annotations.Piggyback
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_ENTER
import dev.jianastrero.journey.annotations.Step

// Five-step checkout with two payment exits from SelectPayment:
//   toCard  → enter card details → Processing
//   toWallet → skip card entry, go straight to Processing (e.g. Apple Pay / wallet)
@Journey
sealed interface Checkout : JourneyStep {

    @Step
    @Exit("toEnterAddress", EnterAddress::class)
    data object ReviewCart : Checkout

    // User enters address on this step; passes it forward via controller.toSelectPayment(address).
    @Step
    @Exit("toSelectPayment", SelectPayment::class)
    data object EnterAddress : Checkout

    // Two payment exits demonstrate branching within a journey.
    @Step
    @Exit("toCard", EnterCardDetails::class)
    @Exit("toWallet", Processing::class)
    data class SelectPayment(val address: String) : Checkout

    @Step
    @Exit("toProcessing", Processing::class)
    data class EnterCardDetails(val address: String) : Checkout

    @Step
    @Exit("toDone", Done::class)
    @Piggyback("analytics:checkout_processing", on = ON_ENTER)
    data class Processing(val address: String, val paymentMethod: String) : Checkout

    @Step
    @Piggyback("analytics:purchase_completed", on = ON_ENTER)
    data object Done : Checkout
}
