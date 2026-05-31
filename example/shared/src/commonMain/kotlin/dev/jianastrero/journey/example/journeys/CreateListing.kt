package dev.jianastrero.journey.example

import dev.jianastrero.journey.JourneyStep
import dev.jianastrero.journey.annotations.Exit
import dev.jianastrero.journey.annotations.Journey
import dev.jianastrero.journey.annotations.Piggyback
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_ENTER
import dev.jianastrero.journey.annotations.Step

// Four-step wizard: title+category → description → price → review → published.
// Data is threaded through each step so screens are fully self-contained.
@Journey
sealed interface CreateListing : JourneyStep {

    @Step
    @Exit("toEnterDescription", EnterDescription::class)
    data object EnterTitle : CreateListing

    @Step
    @Exit("toEnterPrice", EnterPrice::class)
    data class EnterDescription(val title: String, val category: String) : CreateListing

    @Step
    @Exit("toReview", Review::class)
    data class EnterPrice(val title: String, val category: String, val description: String) : CreateListing

    @Step
    @Exit("toPublish", Published::class)
    data class Review(val title: String, val category: String, val description: String, val price: String) : CreateListing

    // Terminal — KSP generates finish() on CreateListingPublishedController.
    @Step
    @Piggyback("analytics:listing_created", on = ON_ENTER)
    data class Published(val title: String, val category: String, val description: String, val price: String) : CreateListing
}
