@file:Suppress("InvalidPackageDeclaration", "unused")

package dev.jianastrero.journey.example

import dev.jianastrero.journey.JourneyStep
import dev.jianastrero.journey.annotations.Exit
import dev.jianastrero.journey.annotations.Journey
import dev.jianastrero.journey.annotations.Piggyback
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_ENTER
import dev.jianastrero.journey.annotations.PiggybackTrigger.ON_EXIT
import dev.jianastrero.journey.annotations.Step

// Edit flow mirrors CreateListing. The initial EnterTitle screen pre-populates
// its fields from AppState.editingListingId before the user can make changes.
@Journey
sealed interface EditListing : JourneyStep {

    // ON_ENTER: edit session starts on arrival.
    @Step
    @Piggyback("analytics:listing_edit_started", on = ON_ENTER)
    @Exit("toEnterDescription", EnterDescription::class)
    data object EnterTitle : EditListing

    // ON_EXIT: title/category edits are committed on departure.
    @Step
    @Piggyback("analytics:listing_edit_step2", on = ON_EXIT)
    @Exit("toEnterPrice", EnterPrice::class)
    data class EnterDescription(val title: String, val category: String) : EditListing

    // ON_EXIT: description edits committed on departure.
    @Step
    @Piggyback("analytics:listing_edit_step3", on = ON_EXIT)
    @Exit("toDone", Done::class)
    data class EnterPrice(val title: String, val category: String, val description: String) : EditListing

    // analytics on arrival confirms the update completed;
    // cache invalidation deferred to exit so it runs after the Done screen is shown.
    @Step
    @Piggyback("analytics:listing_updated", on = ON_ENTER)
    @Piggyback("cache:invalidate_listing", on = ON_EXIT)
    data class Done(val title: String, val category: String, val description: String, val price: String) : EditListing
}
