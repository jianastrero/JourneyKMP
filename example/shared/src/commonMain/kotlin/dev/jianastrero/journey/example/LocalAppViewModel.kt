package dev.jianastrero.journey.example

import androidx.compose.runtime.compositionLocalOf

// Kind'a like a singleton but make it look like an injected object via composition local
val LocalAppViewModel = compositionLocalOf<AppViewModel> { error("LocalAppViewModel not provided") }
