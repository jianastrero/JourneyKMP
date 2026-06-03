package dev.jianastrero.journey.example.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import dev.jianastrero.journey.example.journeys.SignInJourneyHost
import dev.jianastrero.journey.example.journeys.SignInView
import dev.jianastrero.journey.example.journeys.SignUpJourneyHost
import dev.jianastrero.journey.example.journeys.SignUpView
import dev.jianastrero.journey.example.screens.auth.SignInDoneScreen
import dev.jianastrero.journey.example.screens.auth.SignInEnterCredentialsScreen
import dev.jianastrero.journey.example.screens.auth.SignInEnterPasswordScreen
import dev.jianastrero.journey.example.screens.auth.SignInSSOLoadingScreen
import dev.jianastrero.journey.example.screens.auth.SignUpDoneScreen
import dev.jianastrero.journey.example.screens.auth.SignUpEnterEmailScreen
import dev.jianastrero.journey.example.screens.auth.SignUpEnterPasswordScreen
import dev.jianastrero.journey.example.screens.auth.SignUpEnterUsernameScreen
import dev.jianastrero.journey.example.screens.auth.SignUpSSOLoadingScreen

private sealed interface AuthDest {
    data object SignIn : AuthDest
    data object SignUp : AuthDest
}

private val authDestSaver = listSaver<SnapshotStateList<Any>, String>(
    save = { list ->
        list.map { dest ->
            when (dest) {
                is AuthDest.SignIn -> "SignIn"
                is AuthDest.SignUp -> "SignUp"
                else -> ""
            }
        }
    },
    restore = { saved ->
        val list = mutableStateListOf<Any>()
        saved.forEach { name ->
            when (name) {
                "SignIn" -> list.add(AuthDest.SignIn)
                "SignUp" -> list.add(AuthDest.SignUp)
            }
        }
        list
    }
)

@Composable
internal fun AuthFlow(onSignedIn: () -> Unit) {
    // Auth modes are peers — switching replaces the current entry rather than pushing.
    val backStack = rememberSaveable(saver = authDestSaver) { mutableStateListOf(AuthDest.SignIn) }
    NavDisplay(backStack = backStack, onBack = { backStack.removeLastOrNull() }) { dest ->
        when (dest) {
            is AuthDest.SignIn -> NavEntry(dest) {
                SignInJourneyHost(onFinish = onSignedIn) { view ->
                    when (view) {
                        is SignInView.EnterCredentials -> SignInEnterCredentialsScreen(
                            controller = view.controller,
                            onSwitchToSignUp = { backStack[0] = AuthDest.SignUp },
                        )
                        is SignInView.EnterPassword -> SignInEnterPasswordScreen(view.step, view.controller)
                        is SignInView.SSOLoading -> SignInSSOLoadingScreen(view.step, view.controller)
                        is SignInView.Done -> SignInDoneScreen(view.controller)
                    }
                }
            }
            is AuthDest.SignUp -> NavEntry(dest) {
                SignUpJourneyHost(onFinish = onSignedIn) { view ->
                    when (view) {
                        is SignUpView.EnterUsername -> SignUpEnterUsernameScreen(
                            controller = view.controller,
                            onSwitchToSignIn = { backStack[0] = AuthDest.SignIn },
                        )
                        is SignUpView.EnterEmail -> SignUpEnterEmailScreen(view.step, view.controller)
                        is SignUpView.EnterPassword -> SignUpEnterPasswordScreen(view.step, view.controller)
                        is SignUpView.SSOLoading -> SignUpSSOLoadingScreen(view.step, view.controller)
                        is SignUpView.Done -> SignUpDoneScreen(view.controller)
                    }
                }
            }
            else -> NavEntry(dest) {}
        }
    }
}
