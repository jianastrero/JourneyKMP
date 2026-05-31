package dev.jianastrero.journey.example.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import dev.jianastrero.journey.example.SignUpDoneController

@Composable
internal fun SignUpDoneScreen(controller: SignUpDoneController) {
    LaunchedEffect(Unit) { controller.finish() }
}
