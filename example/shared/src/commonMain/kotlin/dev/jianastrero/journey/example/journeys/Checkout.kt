package dev.jianastrero.journey.example

import dev.jianastrero.journey.JourneyStep
import dev.jianastrero.journey.annotations.Exit
import dev.jianastrero.journey.annotations.Journey
import dev.jianastrero.journey.annotations.Piggyback
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_ENTER
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_EXIT
import dev.jianastrero.journey.annotations.Step

// Five-step checkout with two payment exits from SelectPayment:
//   toCard  → enter card details → Processing
//   toWallet → skip card entry, go straight to Processing (e.g. Bazaar Wallet)
@Journey
sealed interface Checkout : JourneyStep {

    // ON_ENTER: marks checkout session start immediately on arrival.
    // ON_EXIT: cart review is complete — user made a decision (proceed or abandon).
    @Step
    @Piggyback("analytics:checkout_started", on = ON_ENTER)
    @Piggyback("analytics:cart_review_complete", on = ON_EXIT)
    @Exit("toEnterAddress", EnterAddress::class)
    data object ReviewCart : Checkout

    // ON_ENTER: screen view for funnel visibility at this step.
    // ON_EXIT: address step complete on departure.
    // User enters address on this step; passes it forward via controller.toSelectPayment(address).
    @Step
    @Piggyback("analytics:address_screen_view", on = ON_ENTER)
    @Piggyback("analytics:checkout_step_address", on = ON_EXIT)
    @Exit("toSelectPayment", SelectPayment::class)
    data object EnterAddress : Checkout

    // ON_ENTER: screen view captured on arrival.
    // ON_EXIT: payment method confirmed on departure (works for both card and wallet paths).
    // Two payment exits demonstrate branching within a journey.
    @Step
    @Piggyback("analytics:payment_screen_view", on = ON_ENTER)
    @Piggyback("analytics:checkout_step_payment", on = ON_EXIT)
    @Exit("toCard", EnterCardDetails::class)
    @Exit("toWallet", Processing::class)
    data class SelectPayment(val address: String) : Checkout

    // ON_ENTER: fires only for the card path — useful for card vs wallet split analysis.
    // ON_EXIT: card details submitted on departure (whether successful or abandoned).
    @Step
    @Piggyback("analytics:checkout_payment_card", on = ON_ENTER)
    @Piggyback("analytics:card_details_submitted", on = ON_EXIT)
    @Exit("toProcessing", Processing::class)
    data class EnterCardDetails(val address: String) : Checkout

    // ON_ENTER for funnel tracking (payment is in flight); ON_EXIT for the audit log
    // so it captures the attempt result regardless of success or timeout.
    @Step
    @Piggyback("analytics:checkout_processing", on = ON_ENTER)
    @Piggyback("log:payment_initiated", on = ON_EXIT)
    @Exit("toDone", Done::class)
    data class Processing(val address: String, val paymentMethod: String) : Checkout

    // Terminal step: analytics + push notification on arrival;
    // cart cache cleared on exit (after the user has seen the confirmation).
    // ON_ENTER fires the immediate success signals; ON_EXIT handles deferred cleanup.
    @Step
    @Piggyback("analytics:purchase_completed", on = ON_ENTER)
    @Piggyback("notification:order_confirmed", on = ON_ENTER)
    @Piggyback("cache:clear_cart", on = ON_EXIT)
    data object Done : Checkout
}
