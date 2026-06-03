@file:Suppress("unused")

package dev.jianastrero.journey.example

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dev.jianastrero.journey.example.model.CartItem
import dev.jianastrero.journey.example.model.Listing
import dev.jianastrero.journey.example.model.User

private const val INITIAL_LISTING_ID = 100

class AppViewModel : ViewModel() {
    var currentUser: User? by mutableStateOf(null)
    val listings = mutableStateListOf<Listing>()
    val cart = mutableStateListOf<CartItem>()
    var editingListingId: String? by mutableStateOf(null)
    var deletingListingId: String? by mutableStateOf(null)
    private var nextId = INITIAL_LISTING_ID

    init { listings.addAll(sampleListings) }

    fun signIn(username: String) {
        currentUser = User("user1", username, "$username@example.com", username.replaceFirstChar { it.uppercase() })
    }

    fun signUp(username: String, email: String) {
        currentUser = User("user1", username, email, username.replaceFirstChar { it.uppercase() })
    }

    fun signOut() {
        currentUser = null
        cart.clear()
    }

    fun addToCart(listing: Listing) {
        val idx = cart.indexOfFirst { it.listing.id == listing.id }
        if (idx >= 0) {
            cart[idx] = cart[idx].copy(quantity = cart[idx].quantity + 1)
        } else {
            cart.add(CartItem(listing, 1))
        }
    }

    fun updateCartQuantity(listingId: String, delta: Int) {
        val idx = cart.indexOfFirst { it.listing.id == listingId }
        if (idx < 0) return
        val newQty = cart[idx].quantity + delta
        if (newQty <= 0) cart.removeAt(idx) else cart[idx] = cart[idx].copy(quantity = newQty)
    }

    fun createListing(title: String, category: String, description: String, price: String) {
        val userId = currentUser?.id ?: return
        listings.add(Listing("listing${nextId++}", title, description, price.toDoubleOrNull() ?: 0.0, category, userId))
    }

    fun updateListing(id: String, title: String, category: String, description: String, price: String) {
        val idx = listings.indexOfFirst { it.id == id }
        if (idx < 0) return
        listings[idx] = listings[idx].copy(
            title = title, category = category, description = description,
            price = price.toDoubleOrNull() ?: listings[idx].price
        )
    }

    fun deleteListing(id: String) {
        listings.removeAll { it.id == id }
        cart.removeAll { it.listing.id == id }
    }

    fun clearCart() { cart.clear() }

    val myListings: List<Listing> get() = listings.filter { it.sellerId == currentUser?.id }
    val cartTotal: Double get() = cart.sumOf { it.listing.price * it.quantity }
    val cartItemCount: Int get() = cart.sumOf { it.quantity }
}

@Suppress("MagicNumber")
private val sampleListings = listOf(
    Listing(
        "1",
        "Vintage Leather Jacket",
        "Classic biker jacket in genuine leather, lightly worn with beautiful patina. Size M.",
        89.99,
        "Clothing",
        "seller1"
    ),
    Listing(
        "2",
        "Sony WH-1000XM5 Headphones",
        "Industry-leading noise cancellation. Barely used, includes original box and all accessories.",
        149.00,
        "Electronics",
        "seller2"
    ),
    Listing(
        "3",
        "The Art of Clean Code",
        "Paperback in excellent condition. A timeless guide to writing readable, maintainable software.",
        29.99,
        "Books",
        "seller3"
    ),
    Listing(
        "4",
        "Artisan Ceramic Mug Set (6 pcs)",
        "Handmade mugs with food-safe glaze. Each one is unique — perfect for gifting.",
        34.99,
        "Home",
        "seller1"
    ),
    Listing(
        "5",
        "Nike Air Max Running Shoes",
        "Size US 10. Worn twice, excellent condition. Original box included.",
        79.99,
        "Clothing",
        "seller2"
    ),
    Listing(
        "6",
        "Smart LED Desk Lamp",
        "USB-C rechargeable, adjustable colour temperature 2700 K–6500 K, touch dimmer.",
        49.99,
        "Electronics",
        "seller3"
    ),
    Listing(
        "7",
        "Succulent Collection (5 plants)",
        "Low-maintenance, pet-friendly varieties. Includes terracotta pots.",
        24.99,
        "Home",
        "seller1"
    ),
    Listing(
        "8",
        "Kotlin in Action, 2nd Ed.",
        "Factory sealed, never opened. Comprehensive guide by JetBrains engineers.",
        44.99,
        "Books",
        "seller2"
    ),
)
