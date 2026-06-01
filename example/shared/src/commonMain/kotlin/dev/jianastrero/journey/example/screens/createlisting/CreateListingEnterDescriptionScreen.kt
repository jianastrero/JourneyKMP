@file:Suppress("unused")

package dev.jianastrero.journey.example.screens.createlisting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import dev.jianastrero.journey.example.CreateListing
import dev.jianastrero.journey.example.CreateListingEnterDescriptionController
import dev.jianastrero.journey.example.screens.StepButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CreateListingEnterDescriptionScreen(
    step: CreateListing.EnterDescription,
    controller: CreateListingEnterDescriptionController,
) {
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("New listing", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "Step 2 of 4",
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
            Text("Describe your item", style = MaterialTheme.typography.headlineSmall)
            Text(
                "Good descriptions include condition, size, and any defects",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                placeholder = { Text("e.g. Excellent condition, worn twice…") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                maxLines = 6,
            )
            Spacer(Modifier.weight(1f))
            StepButton(
                label = "Next: Price",
                enabled = description.isNotBlank(),
                onClick = { controller.toEnterPrice(step.title, step.category, description.trim()) },
            )
        }
    }
}
