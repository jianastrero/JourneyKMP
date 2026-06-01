@file:Suppress("unused")

package dev.jianastrero.journey.example.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.jianastrero.journey.example.model.Listing
import dev.jianastrero.journey.example.screens.toPrice
import dev.jianastrero.journey.example.theme.BooksColor
import dev.jianastrero.journey.example.theme.ClothingColor
import dev.jianastrero.journey.example.theme.ElectronicsColor
import dev.jianastrero.journey.example.theme.HomeColor
import dev.jianastrero.journey.example.theme.OtherColor

@Composable
internal fun ListingCard(listing: Listing, onAddToCart: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column {
            CategoryHeader(listing.category)
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    listing.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    listing.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "$${listing.price.toPrice()}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Button(
                        onClick = onAddToCart,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                    ) {
                        Text("Add", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryHeader(category: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(categoryColor(category)),
        contentAlignment = Alignment.BottomStart,
    ) {
        Text(
            category,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

internal fun categoryColor(category: String): Color = when (category) {
    "Electronics" -> ElectronicsColor
    "Clothing" -> ClothingColor
    "Books" -> BooksColor
    "Home" -> HomeColor
    else -> OtherColor
}
