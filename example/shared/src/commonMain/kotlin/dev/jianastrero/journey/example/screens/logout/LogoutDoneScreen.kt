package dev.jianastrero.journey.example.screens.logout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.jianastrero.journey.example.journeys.LogoutDoneController

@Composable
internal fun LogoutDoneScreen(controller: LogoutDoneController) {
    LaunchedEffect(Unit) { controller.finish() }
}
