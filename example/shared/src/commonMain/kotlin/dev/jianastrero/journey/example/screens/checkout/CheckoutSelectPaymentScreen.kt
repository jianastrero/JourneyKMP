package dev.jianastrero.journey.example.screens.checkout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.jianastrero.journey.example.Checkout
import dev.jianastrero.journey.example.CheckoutSelectPaymentController
import dev.jianastrero.journey.example.screens.StepButton

private enum class PaymentMethod { Card, Wallet }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CheckoutSelectPaymentScreen(step: Checkout.SelectPayment, controller: CheckoutSelectPaymentController) {
    var selected by remember { mutableStateOf(PaymentMethod.Card) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment method") },
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
            Text("How would you like to pay?", style = MaterialTheme.typography.headlineSmall)
            PaymentOptionCard(
                icon = { Icon(Icons.Default.CreditCard, contentDescription = null, modifier = Modifier.size(28.dp)) },
                title = "Credit / Debit card",
                subtitle = "Visa, Mastercard, Amex",
                selected = selected == PaymentMethod.Card,
                onClick = { selected = PaymentMethod.Card },
            )
            PaymentOptionCard(
                icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, modifier = Modifier.size(28.dp), tint = MaterialTheme.colorScheme.secondary) },
                title = "Bazaar Wallet",
                subtitle = "Pay instantly with your balance",
                selected = selected == PaymentMethod.Wallet,
                onClick = { selected = PaymentMethod.Wallet },
            )
            Spacer(Modifier.weight(1f))
            StepButton(
                label = if (selected == PaymentMethod.Card) "Enter card details" else "Pay with Wallet",
                enabled = true,
                onClick = {
                    when (selected) {
                        PaymentMethod.Card -> controller.toCard(step.address)
                        PaymentMethod.Wallet -> controller.toWallet(step.address, "BazaarWallet")
                    }
                },
            )
        }
    }
}

@Composable
private fun PaymentOptionCard(
    icon: @Composable () -> Unit,
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = if (selected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 0.dp else 1.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            icon()
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            RadioButton(selected = selected, onClick = onClick)
        }
    }
}
