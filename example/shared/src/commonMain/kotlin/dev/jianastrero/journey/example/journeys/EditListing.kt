package dev.jianastrero.journey.example

import dev.jianastrero.journey.JourneyStep
import dev.jianastrero.journey.annotations.Exit
import dev.jianastrero.journey.annotations.Journey
import dev.jianastrero.journey.annotations.Piggyback
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_ENTER
import dev.jianastrero.journey.annotations.Step

// Edit flow mirrors CreateListing. The initial EnterTitle screen pre-populates
// its fields from AppState.editingListingId before the user can make changes.
@Journey
sealed interface EditListing : JourneyStep {

    @Step
    @Exit("toEnterDescription", EnterDescription::class)
    data object EnterTitle : EditListing

    @Step
    @Exit("toEnterPrice", EnterPrice::class)
    data class EnterDescription(val title: String, val category: String) : EditListing

    @Step
    @Exit("toDone", Done::class)
    data class EnterPrice(val title: String, val category: String, val description: String) : EditListing

    @Step
    @Piggyback("analytics:listing_updated", on = ON_ENTER)
    data class Done(val title: String, val category: String, val description: String, val price: String) : EditListing
}
