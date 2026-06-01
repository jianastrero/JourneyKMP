@file:Suppress("InvalidPackageDeclaration", "unused")

package dev.jianastrero.journey.example

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.jianastrero.journey.example.screens.auth.SignInDoneScreen
import dev.jianastrero.journey.example.screens.auth.SignInEnterCredentialsScreen
import dev.jianastrero.journey.example.screens.auth.SignInEnterPasswordScreen
import dev.jianastrero.journey.example.screens.auth.SignInSSOLoadingScreen
import dev.jianastrero.journey.example.screens.auth.SignUpDoneScreen
import dev.jianastrero.journey.example.screens.auth.SignUpEnterEmailScreen
import dev.jianastrero.journey.example.screens.auth.SignUpEnterPasswordScreen
import dev.jianastrero.journey.example.screens.auth.SignUpEnterUsernameScreen
import dev.jianastrero.journey.example.screens.auth.SignUpSSOLoadingScreen

private enum class AuthMode { SignIn, SignUp }

@Composable
internal fun AuthFlow(onSignedIn: () -> Unit) {
    var mode by remember { mutableStateOf(AuthMode.SignIn) }
    when (mode) {
        AuthMode.SignIn -> SignInJourneyHost(onFinish = onSignedIn) { view ->
            when (view) {
                is SignInView.EnterCredentials -> SignInEnterCredentialsScreen(
                    controller = view.controller,
                    onSwitchToSignUp = { mode = AuthMode.SignUp }
                )
                is SignInView.EnterPassword -> SignInEnterPasswordScreen(view.step, view.controller)
                is SignInView.SSOLoading -> SignInSSOLoadingScreen(view.step, view.controller)
                is SignInView.Done -> SignInDoneScreen(view.controller)
            }
        }
        AuthMode.SignUp -> SignUpJourneyHost(onFinish = onSignedIn) { view ->
            when (view) {
                is SignUpView.EnterUsername -> SignUpEnterUsernameScreen(
                    controller = view.controller,
                    onSwitchToSignIn = { mode = AuthMode.SignIn }
                )
                is SignUpView.EnterEmail -> SignUpEnterEmailScreen(view.step, view.controller)
                is SignUpView.EnterPassword -> SignUpEnterPasswordScreen(view.step, view.controller)
                is SignUpView.SSOLoading -> SignUpSSOLoadingScreen(view.step, view.controller)
                is SignUpView.Done -> SignUpDoneScreen(view.controller)
            }
        }
    }
}
