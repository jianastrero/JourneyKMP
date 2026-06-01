package dev.jianastrero.journey.example.screens.deletelisting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.jianastrero.journey.example.journeys.DeleteListingDoneController

@Composable
internal fun DeleteListingDoneScreen(controller: DeleteListingDoneController) {
    LaunchedEffect(Unit) { controller.finish() }
}
