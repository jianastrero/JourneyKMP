package dev.jianastrero.journey.example

import dev.jianastrero.journey.JourneyStep
import dev.jianastrero.journey.annotations.Exit
import dev.jianastrero.journey.annotations.Journey
import dev.jianastrero.journey.annotations.Piggyback
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_ENTER
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_EXIT
import dev.jianastrero.journey.annotations.Step

// Minimal two-step confirmation: show the listing to delete, then confirm.
// The listing to delete is looked up from AppState.deletingListingId.
@Journey
sealed interface DeleteListing : JourneyStep {

    // ON_ENTER: initiation fires immediately — measures how often users open the
    // dialog vs how often they actually confirm (Confirm vs Done completion rate).
    // ON_EXIT: decision made on departure, regardless of confirm or cancel — enables
    //          computing the confirmation rate from a single event stream.
    @Step
    @Piggyback("analytics:listing_delete_initiated", on = ON_ENTER)
    @Piggyback("analytics:listing_delete_decision", on = ON_EXIT)
    @Exit("toDone", Done::class)
    data object Confirm : DeleteListing

    // analytics on arrival (delete confirmed); cache invalidation on exit
    // so stale data is cleared after the Done screen has been displayed.
    @Step
    @Piggyback("analytics:listing_deleted", on = ON_ENTER)
    @Piggyback("cache:invalidate_listing", on = ON_EXIT)
    data object Done : DeleteListing
}
