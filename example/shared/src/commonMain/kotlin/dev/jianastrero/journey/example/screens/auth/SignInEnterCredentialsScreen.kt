@file:Suppress("unused")

package dev.jianastrero.journey.example.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import dev.jianastrero.journey.example.journeys.SignInEnterCredentialsController
import dev.jianastrero.journey.example.theme.Violet600
import dev.jianastrero.journey.example.theme.Violet900

@Composable
internal fun SignInEnterCredentialsScreen(
    controller: SignInEnterCredentialsController,
    onSwitchToSignUp: () -> Unit,
) {
    var username by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        AuthHeader(title = "Bazaar", subtitle = "Your marketplace for everything")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Welcome back", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Sign in to continue",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Button(
                onClick = { if (username.isNotBlank()) controller.toPassword(username.trim()) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) {
                Text("Continue", style = MaterialTheme.typography.labelLarge)
            }
            SSODivider()
            SSOButtons(
                onGoogle = { controller.toSSO("Google") },
                onApple = { controller.toSSO("Apple") },
            )
            Spacer(Modifier.weight(1f))
            SwitchAuthLink(
                message = "Don't have an account?",
                actionText = "Sign up",
                onClick = onSwitchToSignUp,
            )
        }
    }
}

@Composable
internal fun AuthHeader(title: String, subtitle: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Brush.verticalGradient(listOf(Violet900, Violet600))),
        contentAlignment = Alignment.BottomStart,
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
            )
        }
    }
}

@Composable
internal fun SSODivider() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        HorizontalDivider(modifier = Modifier.weight(1f))
        Text(
            "  or continue with  ",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HorizontalDivider(modifier = Modifier.weight(1f))
    }
}

@Composable
internal fun SSOButtons(onGoogle: () -> Unit, onApple: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedButton(
            onClick = onGoogle,
            modifier = Modifier.fillMaxWidth().height(48.dp),
        ) {
            SSOButtonLabel(letter = "G", letterColor = Color(0xFF4285F4), label = "Continue with Google")
        }
        OutlinedButton(
            onClick = onApple,
            modifier = Modifier.fillMaxWidth().height(48.dp),
        ) {
            SSOButtonLabel(
                letter = "A",
                letterColor = MaterialTheme.colorScheme.onSurface,
                label = "Continue with Apple"
            )
        }
    }
}

@Composable
private fun SSOButtonLabel(letter: String, letterColor: Color, label: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(letter, color = letterColor, fontWeight = FontWeight.Bold)
        }
        Text(label, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
internal fun SwitchAuthLink(message: String, actionText: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            buildAnnotatedString {
                append("$message ")
                withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)) {
                    append(actionText)
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable(onClick = onClick),
        )
    }
}
