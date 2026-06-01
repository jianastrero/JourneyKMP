@file:Suppress("unused")

package dev.jianastrero.journey.example

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.jianastrero.journey.example.theme.ShopTheme

private enum class AppScreen { Auth, Main }

@Composable
fun App() {
    ShopTheme {
        var screen by remember { mutableStateOf(AppScreen.Auth) }
        when (screen) {
            AppScreen.Auth -> AuthFlow(onSignedIn = { screen = AppScreen.Main })
            AppScreen.Main -> MainScreen(onSignedOut = {
                AppState.signOut()
                screen = AppScreen.Auth
            })
        }
    }
}
