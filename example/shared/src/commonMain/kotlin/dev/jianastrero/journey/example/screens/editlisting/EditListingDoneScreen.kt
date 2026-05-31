package dev.jianastrero.journey.example.screens.editlisting

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.jianastrero.journey.example.EditListingDoneController

@Composable
internal fun EditListingDoneScreen(controller: EditListingDoneController) {
    LaunchedEffect(Unit) { controller.finish() }
}
