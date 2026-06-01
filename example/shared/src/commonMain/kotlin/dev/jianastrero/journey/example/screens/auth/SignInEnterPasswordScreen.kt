@file:Suppress("unused")

package dev.jianastrero.journey.example.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import dev.jianastrero.journey.example.AppState
import dev.jianastrero.journey.example.SignIn
import dev.jianastrero.journey.example.SignInEnterPasswordController

@Composable
private fun PasswordInputField(
    password: String,
    passwordVisible: Boolean,
    onPasswordChange: (String) -> Unit,
    onVisibilityToggle: () -> Unit,
) {
    val transformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    val iconDescription = if (passwordVisible) "Hide password" else "Show password"
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Password") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = transformation,
        trailingIcon = {
            IconButton(onClick = onVisibilityToggle) {
                Icon(
                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = iconDescription,
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SignInEnterPasswordScreen(step: SignIn.EnterPassword, controller: SignInEnterPasswordController) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sign in") },
                navigationIcon = {
                    IconButton(onClick = { controller.back() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Enter your password", style = MaterialTheme.typography.headlineMedium)
            Text(
                "Signing in as ${step.username}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))
            PasswordInputField(
                password = password,
                passwordVisible = passwordVisible,
                onPasswordChange = { password = it },
                onVisibilityToggle = { passwordVisible = !passwordVisible },
            )
            Button(
                onClick = {
                    if (password.isNotBlank()) {
                        AppState.signIn(step.username)
                        controller.toDone()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) {
                Text("Sign in", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}
