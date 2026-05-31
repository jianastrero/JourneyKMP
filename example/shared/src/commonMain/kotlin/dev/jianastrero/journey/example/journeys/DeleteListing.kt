package dev.jianastrero.journey.example

import dev.jianastrero.journey.JourneyStep
import dev.jianastrero.journey.annotations.Exit
import dev.jianastrero.journey.annotations.Journey
import dev.jianastrero.journey.annotations.Piggyback
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_ENTER
import dev.jianastrero.journey.annotations.Step

// Minimal two-step confirmation: show the listing to delete, then confirm.
// The listing to delete is looked up from AppState.deletingListingId.
@Journey
sealed interface DeleteListing : JourneyStep {

    @Step
    @Exit("toDone", Done::class)
    data object Confirm : DeleteListing

    @Step
    @Piggyback("analytics:listing_deleted", on = ON_ENTER)
    data object Done : DeleteListing
}
