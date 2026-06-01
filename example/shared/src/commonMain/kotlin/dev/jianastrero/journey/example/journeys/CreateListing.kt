package dev.jianastrero.journey.example

import dev.jianastrero.journey.JourneyStep
import dev.jianastrero.journey.annotations.Exit
import dev.jianastrero.journey.annotations.Journey
import dev.jianastrero.journey.annotations.Piggyback
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_ENTER
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_EXIT
import dev.jianastrero.journey.annotations.Step

// Four-step wizard: title+category → description → price → review → published.
// Data is threaded through each step so screens are fully self-contained.
@Journey
sealed interface CreateListing : JourneyStep {

    // ON_ENTER: funnel entry fires even if the user cancels immediately.
    @Step
    @Piggyback("analytics:listing_create_started", on = ON_ENTER)
    @Exit("toEnterDescription", EnterDescription::class)
    data object EnterTitle : CreateListing

    // ON_EXIT: step 1 is complete when the user leaves with a title and category.
    @Step
    @Piggyback("analytics:listing_funnel_step2", on = ON_EXIT)
    @Exit("toEnterPrice", EnterPrice::class)
    data class EnterDescription(val title: String, val category: String) : CreateListing

    // ON_EXIT: step 2 complete on departure.
    @Step
    @Piggyback("analytics:listing_funnel_step3", on = ON_EXIT)
    @Exit("toReview", Review::class)
    data class EnterPrice(val title: String, val category: String, val description: String) : CreateListing

    // ON_ENTER: arriving at review is a high-intent signal worth capturing immediately.
    // ON_EXIT: fires when the user publishes or goes back — lets you split
    //          "reviewed and published" vs "reviewed and abandoned" in analytics.
    @Step
    @Piggyback("analytics:listing_funnel_review", on = ON_ENTER)
    @Piggyback("analytics:listing_publish_decision", on = ON_EXIT)
    @Exit("toPublish", Published::class)
    data class Review(val title: String, val category: String, val description: String, val price: String) : CreateListing

    // Terminal — KSP generates finish() on CreateListingPublishedController.
    // analytics on arrival; notification deferred to exit so it fires after UI settles.
    @Step
    @Piggyback("analytics:listing_created", on = ON_ENTER)
    @Piggyback("notification:listing_live", on = ON_EXIT)
    data class Published(val title: String, val category: String, val description: String, val price: String) : CreateListing
}
