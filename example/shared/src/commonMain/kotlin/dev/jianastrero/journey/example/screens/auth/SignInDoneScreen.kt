package dev.jianastrero.journey.example.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.jianastrero.journey.example.journeys.SignInDoneController

@Composable
internal fun SignInDoneScreen(controller: SignInDoneController) {
    LaunchedEffect(Unit) { controller.finish() }
}
