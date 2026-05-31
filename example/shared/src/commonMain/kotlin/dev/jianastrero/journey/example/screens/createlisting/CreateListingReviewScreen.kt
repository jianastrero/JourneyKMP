package dev.jianastrero.journey.example.screens.createlisting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.jianastrero.journey.example.AppState
import dev.jianastrero.journey.example.CreateListing
import dev.jianastrero.journey.example.CreateListingReviewController
import dev.jianastrero.journey.example.screens.StepButton
import dev.jianastrero.journey.example.screens.toPrice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CreateListingReviewScreen(step: CreateListing.Review, controller: CreateListingReviewController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("New listing", style = MaterialTheme.typography.titleMedium)
                        Text("Step 4 of 4 — Review", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
            Text("Review your listing", style = MaterialTheme.typography.headlineSmall)
            Text(
                "Make sure everything looks good before publishing",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ReviewRow(label = "Title", value = step.title)
                    HorizontalDivider()
                    ReviewRow(label = "Category", value = step.category)
                    HorizontalDivider()
                    ReviewRow(label = "Description", value = step.description)
                    HorizontalDivider()
                    ReviewRow(label = "Price", value = "$${(step.price.toDoubleOrNull() ?: 0.0).toPrice()}")
                }
            }
            Spacer(Modifier.weight(1f))
            StepButton(
                label = "Publish listing",
                enabled = true,
                onClick = {
                    AppState.createListing(step.title, step.category, step.description, step.price)
                    controller.toPublish(step.title, step.category, step.description, step.price)
                },
            )
        }
    }
}

@Composable
private fun ReviewRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}
