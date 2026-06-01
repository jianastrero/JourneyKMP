@file:Suppress("unused")

package dev.jianastrero.journey.example.screens.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.dp
import dev.jianastrero.journey.example.journeys.Checkout
import dev.jianastrero.journey.example.journeys.CheckoutEnterCardDetailsController
import dev.jianastrero.journey.example.screens.StepButton

private data class CardValues(val number: String, val name: String, val expiry: String, val cvv: String)
private data class CardCallbacks(
    val onNumber: (String) -> Unit,
    val onName: (String) -> Unit,
    val onExpiry: (String) -> Unit,
    val onCvv: (String) -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CheckoutEnterCardScreen(step: Checkout.EnterCardDetails, controller: CheckoutEnterCardDetailsController) {
    var cardNumber by remember { mutableStateOf("") }
    var cardName by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    val isValid = cardNumber.replace(" ", "").length >= 12 && cardName.isNotBlank() &&
        expiry.length >= 4 && cvv.length >= 3

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Card details") },
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
                .padding(horizontal = 24.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Enter your card", style = MaterialTheme.typography.headlineSmall)
            CardInputForm(
                values = CardValues(cardNumber, cardName, expiry, cvv),
                callbacks = CardCallbacks(
                    onNumber = { raw ->
                        val digits = raw.filter { it.isDigit() }.take(16)
                        cardNumber = digits.chunked(4).joinToString(" ")
                    },
                    onName = { cardName = it },
                    onExpiry = { raw ->
                        val digits = raw.filter { it.isDigit() }.take(4)
                        expiry = if (digits.length > 2) "${digits.take(2)}/${digits.drop(2)}" else digits
                    },
                    onCvv = { cvv = it.filter { c -> c.isDigit() }.take(4) },
                ),
            )
            Spacer(Modifier.weight(1f))
            StepButton(
                label = "Review & pay",
                enabled = isValid,
                onClick = { controller.toProcessing(step.address, "Card ending ${cardNumber.takeLast(4)}") },
            )
        }
    }
}

@Composable
private fun CardInputForm(values: CardValues, callbacks: CardCallbacks) {
    OutlinedTextField(
        value = values.number,
        onValueChange = callbacks.onNumber,
        label = { Text("Card number") },
        placeholder = { Text("1234 5678 9012 3456") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
    )
    OutlinedTextField(
        value = values.name,
        onValueChange = callbacks.onName,
        label = { Text("Name on card") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
    )
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = values.expiry,
            onValueChange = callbacks.onExpiry,
            label = { Text("Expiry") },
            placeholder = { Text("MM/YY") },
            modifier = Modifier.weight(1f),
            singleLine = true,
        )
        OutlinedTextField(
            value = values.cvv,
            onValueChange = callbacks.onCvv,
            label = { Text("CVV") },
            modifier = Modifier.width(100.dp),
            singleLine = true,
        )
    }
}
