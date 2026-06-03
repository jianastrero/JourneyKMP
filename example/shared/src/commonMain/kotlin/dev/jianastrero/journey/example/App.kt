@file:Suppress("unused")

package dev.jianastrero.journey.example

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import dev.jianastrero.journey.example.screens.AuthFlow
import dev.jianastrero.journey.example.screens.MainScreen
import dev.jianastrero.journey.example.theme.ShopTheme

private sealed interface AppRoute {
    data object Auth : AppRoute
    data object Main : AppRoute
}

private val appRouteSaver = listSaver<SnapshotStateList<Any>, String>(
    save = { list ->
        list.map { route ->
            when (route) {
                is AppRoute.Auth -> "Auth"
                is AppRoute.Main -> "Main"
                else -> ""
            }
        }
    },
    restore = { saved ->
        val list = mutableStateListOf<Any>()
        saved.forEach { name ->
            when (name) {
                "Auth" -> list.add(AppRoute.Auth)
                "Main" -> list.add(AppRoute.Main)
            }
        }
        list
    }
)

@Composable
fun App() {
    val vm: AppViewModel = viewModel { AppViewModel() }
    ShopTheme {
        CompositionLocalProvider(LocalAppViewModel provides vm) {
            val backStack = rememberSaveable(saver = appRouteSaver) { mutableStateListOf(AppRoute.Auth) }
            NavDisplay(backStack = backStack, onBack = { backStack.removeLastOrNull() }) { route ->
                when (route) {
                    is AppRoute.Auth -> NavEntry(route) {
                        AuthFlow(onSignedIn = { backStack.add(AppRoute.Main) })
                    }
                    is AppRoute.Main -> NavEntry(route) {
                        val appVm = LocalAppViewModel.current
                        MainScreen(onSignedOut = {
                            appVm.signOut()
                            backStack.clear()
                            backStack.add(AppRoute.Auth)
                        })
                    }
                    else -> NavEntry(route) {}
                }
            }
        }
    }
}
