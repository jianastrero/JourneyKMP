@file:Suppress("unused")

package dev.jianastrero.journey.example.screens.editlisting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import dev.jianastrero.journey.example.AppState
import dev.jianastrero.journey.example.EditListing
import dev.jianastrero.journey.example.EditListingEnterPriceController
import dev.jianastrero.journey.example.screens.StepButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditListingEnterPriceScreen(
    step: EditListing.EnterPrice,
    controller: EditListingEnterPriceController,
) {
    val existing = remember { AppState.listings.firstOrNull { it.id == AppState.editingListingId } }
    var price by remember { mutableStateOf(existing?.price?.toString() ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Edit listing", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "Step 3 of 3",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
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
            Text("Update price", style = MaterialTheme.typography.headlineSmall)
            OutlinedTextField(
                value = price,
                onValueChange = { price = it.filter { c -> c.isDigit() || c == '.' } },
                label = { Text("Price (USD)") },
                prefix = { Text("$") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Spacer(Modifier.weight(1f))
            StepButton(
                label = "Save changes",
                enabled = price.isNotBlank() && price.toDoubleOrNull() != null,
                onClick = {
                    val listingId = AppState.editingListingId
                    if (listingId != null) {
                        AppState.updateListing(listingId, step.title, step.category, step.description, price.trim())
                    }
                    controller.toDone(step.title, step.category, step.description, price.trim())
                },
            )
        }
    }
}
