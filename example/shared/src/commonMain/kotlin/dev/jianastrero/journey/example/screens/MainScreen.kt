package dev.jianastrero.journey.example

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import dev.jianastrero.journey.example.screens.checkout.CheckoutDoneScreen
import dev.jianastrero.journey.example.screens.checkout.CheckoutEnterAddressScreen
import dev.jianastrero.journey.example.screens.checkout.CheckoutEnterCardScreen
import dev.jianastrero.journey.example.screens.checkout.CheckoutProcessingScreen
import dev.jianastrero.journey.example.screens.checkout.CheckoutReviewCartScreen
import dev.jianastrero.journey.example.screens.checkout.CheckoutSelectPaymentScreen
import dev.jianastrero.journey.example.screens.createlisting.CreateListingEnterDescriptionScreen
import dev.jianastrero.journey.example.screens.createlisting.CreateListingEnterPriceScreen
import dev.jianastrero.journey.example.screens.createlisting.CreateListingEnterTitleScreen
import dev.jianastrero.journey.example.screens.createlisting.CreateListingPublishedScreen
import dev.jianastrero.journey.example.screens.createlisting.CreateListingReviewScreen
import dev.jianastrero.journey.example.screens.deletelisting.DeleteListingConfirmScreen
import dev.jianastrero.journey.example.screens.deletelisting.DeleteListingDoneScreen
import dev.jianastrero.journey.example.screens.editlisting.EditListingDoneScreen
import dev.jianastrero.journey.example.screens.editlisting.EditListingEnterDescriptionScreen
import dev.jianastrero.journey.example.screens.editlisting.EditListingEnterPriceScreen
import dev.jianastrero.journey.example.screens.editlisting.EditListingEnterTitleScreen
import dev.jianastrero.journey.example.screens.logout.LogoutConfirmScreen
import dev.jianastrero.journey.example.screens.logout.LogoutDoneScreen
import dev.jianastrero.journey.example.screens.main.CartTab
import dev.jianastrero.journey.example.screens.main.HomeTab
import dev.jianastrero.journey.example.screens.main.MyListingsTab
import dev.jianastrero.journey.example.screens.main.ProfileTab

private enum class MainTab { Home, Listings, Cart, Profile }

private sealed interface ActiveJourney {
    data object None : ActiveJourney
    data object CreateListing : ActiveJourney
    data class EditListing(val listingId: String) : ActiveJourney
    data class DeleteListing(val listingId: String) : ActiveJourney
    data object Checkout : ActiveJourney
    data object Logout : ActiveJourney
}

@Composable
internal fun MainScreen(onSignedOut: () -> Unit) {
    var activeTab by remember { mutableStateOf(MainTab.Home) }
    var activeJourney by remember { mutableStateOf<ActiveJourney>(ActiveJourney.None) }

    when (val journey = activeJourney) {
        is ActiveJourney.None -> MainScaffold(
            activeTab = activeTab,
            onTabSelected = { activeTab = it },
            onCreateListing = { activeJourney = ActiveJourney.CreateListing },
            onEditListing = { id -> AppState.editingListingId = id; activeJourney = ActiveJourney.EditListing(id) },
            onDeleteListing = { id -> AppState.deletingListingId = id; activeJourney = ActiveJourney.DeleteListing(id) },
            onCheckout = { activeJourney = ActiveJourney.Checkout },
            onLogout = { activeJourney = ActiveJourney.Logout },
        )
        is ActiveJourney.CreateListing -> CreateListingJourneyHost(onFinish = { activeJourney = ActiveJourney.None }) { view ->
            when (view) {
                is CreateListingView.EnterTitle -> CreateListingEnterTitleScreen(view.controller, onCancel = { activeJourney = ActiveJourney.None })
                is CreateListingView.EnterDescription -> CreateListingEnterDescriptionScreen(view.step, view.controller)
                is CreateListingView.EnterPrice -> CreateListingEnterPriceScreen(view.step, view.controller)
                is CreateListingView.Review -> CreateListingReviewScreen(view.step, view.controller)
                is CreateListingView.Published -> CreateListingPublishedScreen(view.step, view.controller)
            }
        }
        is ActiveJourney.EditListing -> EditListingJourneyHost(onFinish = { activeJourney = ActiveJourney.None }) { view ->
            when (view) {
                is EditListingView.EnterTitle -> EditListingEnterTitleScreen(journey.listingId, view.controller, onCancel = { activeJourney = ActiveJourney.None })
                is EditListingView.EnterDescription -> EditListingEnterDescriptionScreen(view.step, view.controller)
                is EditListingView.EnterPrice -> EditListingEnterPriceScreen(view.step, view.controller)
                is EditListingView.Done -> EditListingDoneScreen(view.controller)
            }
        }
        is ActiveJourney.DeleteListing -> DeleteListingJourneyHost(onFinish = { activeJourney = ActiveJourney.None }) { view ->
            when (view) {
                is DeleteListingView.Confirm -> DeleteListingConfirmScreen(journey.listingId, view.controller, onCancel = { activeJourney = ActiveJourney.None })
                is DeleteListingView.Done -> DeleteListingDoneScreen(view.controller)
            }
        }
        is ActiveJourney.Checkout -> CheckoutJourneyHost(onFinish = { activeJourney = ActiveJourney.None }) { view ->
            when (view) {
                is CheckoutView.ReviewCart -> CheckoutReviewCartScreen(view.controller)
                is CheckoutView.EnterAddress -> CheckoutEnterAddressScreen(view.controller)
                is CheckoutView.SelectPayment -> CheckoutSelectPaymentScreen(view.step, view.controller)
                is CheckoutView.EnterCardDetails -> CheckoutEnterCardScreen(view.step, view.controller)
                is CheckoutView.Processing -> CheckoutProcessingScreen(view.step, view.controller)
                is CheckoutView.Done -> CheckoutDoneScreen(view.controller)
            }
        }
        is ActiveJourney.Logout -> LogoutJourneyHost(onFinish = onSignedOut) { view ->
            when (view) {
                is LogoutView.Confirm -> LogoutConfirmScreen(view.controller, onCancel = { activeJourney = ActiveJourney.None })
                is LogoutView.Done -> LogoutDoneScreen(view.controller)
            }
        }
    }
}

@Composable
private fun MainScaffold(
    activeTab: MainTab,
    onTabSelected: (MainTab) -> Unit,
    onCreateListing: () -> Unit,
    onEditListing: (String) -> Unit,
    onDeleteListing: (String) -> Unit,
    onCheckout: () -> Unit,
    onLogout: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = activeTab == MainTab.Home,
                    onClick = { onTabSelected(MainTab.Home) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                )
                NavigationBarItem(
                    selected = activeTab == MainTab.Listings,
                    onClick = { onTabSelected(MainTab.Listings) },
                    icon = { Icon(Icons.Default.Store, contentDescription = "My Listings") },
                    label = { Text("Listings") },
                )
                NavigationBarItem(
                    selected = activeTab == MainTab.Cart,
                    onClick = { onTabSelected(MainTab.Cart) },
                    icon = {
                        val count = AppState.cartItemCount
                        BadgedBox(badge = { if (count > 0) Badge { Text("$count") } }) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                        }
                    },
                    label = { Text("Cart") },
                )
                NavigationBarItem(
                    selected = activeTab == MainTab.Profile,
                    onClick = { onTabSelected(MainTab.Profile) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                )
            }
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(bottom = padding.calculateBottomPadding())) {
            when (activeTab) {
                MainTab.Home -> HomeTab(onAddToCart = { listingId ->
                    AppState.listings.firstOrNull { it.id == listingId }?.let { AppState.addToCart(it) }
                })
                MainTab.Listings -> MyListingsTab(
                    onCreateListing = onCreateListing,
                    onEditListing = onEditListing,
                    onDeleteListing = onDeleteListing,
                )
                MainTab.Cart -> CartTab(onCheckout = onCheckout)
                MainTab.Profile -> ProfileTab(onLogout = onLogout)
            }
        }
    }
}
