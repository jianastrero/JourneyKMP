package dev.jianastrero.journey.example.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.jianastrero.journey.example.SignUpEnterUsernameController

@Composable
internal fun SignUpEnterUsernameScreen(
    controller: SignUpEnterUsernameController,
    onSwitchToSignIn: () -> Unit,
) {
    var username by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        AuthHeader(title = "Bazaar", subtitle = "Join millions of buyers & sellers")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Create your account", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Start selling and buying today",
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
                onClick = { if (username.isNotBlank()) controller.toEnterEmail(username.trim()) },
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
                message = "Already have an account?",
                actionText = "Sign in",
                onClick = onSwitchToSignIn,
            )
        }
    }
}
