package dev.jianastrero.journey.example.screens

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
import dev.jianastrero.journey.example.AppState
import dev.jianastrero.journey.example.journeys.CheckoutJourneyHost
import dev.jianastrero.journey.example.journeys.CheckoutView
import dev.jianastrero.journey.example.journeys.CreateListingJourneyHost
import dev.jianastrero.journey.example.journeys.CreateListingView
import dev.jianastrero.journey.example.journeys.DeleteListingJourneyHost
import dev.jianastrero.journey.example.journeys.DeleteListingView
import dev.jianastrero.journey.example.journeys.EditListingJourneyHost
import dev.jianastrero.journey.example.journeys.EditListingView
import dev.jianastrero.journey.example.journeys.LogoutJourneyHost
import dev.jianastrero.journey.example.journeys.LogoutView
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

private data class MainActions(
    val onCreateListing: () -> Unit,
    val onEditListing: (String) -> Unit,
    val onDeleteListing: (String) -> Unit,
    val onCheckout: () -> Unit,
    val onLogout: () -> Unit,
)

@Composable
internal fun MainScreen(onSignedOut: () -> Unit) {
    var activeTab by remember { mutableStateOf(MainTab.Home) }
    var activeJourney by remember { mutableStateOf<ActiveJourney>(ActiveJourney.None) }
    val onJourneyEnd: () -> Unit = { activeJourney = ActiveJourney.None }
    val actions = MainActions(
        onCreateListing = { activeJourney = ActiveJourney.CreateListing },
        onEditListing = { id ->
            AppState.editingListingId = id
            activeJourney = ActiveJourney.EditListing(id)
        },
        onDeleteListing = { id ->
            AppState.deletingListingId = id
            activeJourney = ActiveJourney.DeleteListing(id)
        },
        onCheckout = { activeJourney = ActiveJourney.Checkout },
        onLogout = { activeJourney = ActiveJourney.Logout },
    )
    val journey = activeJourney
    if (journey is ActiveJourney.None) {
        MainScaffold(activeTab = activeTab, onTabSelected = { activeTab = it }, actions = actions)
    } else {
        ActiveJourneyOverlay(journey = journey, onJourneyEnd = onJourneyEnd, onSignedOut = onSignedOut)
    }
}

@Composable
private fun ActiveJourneyOverlay(
    journey: ActiveJourney,
    onJourneyEnd: () -> Unit,
    onSignedOut: () -> Unit,
) {
    when (journey) {
        is ActiveJourney.None -> Unit
        is ActiveJourney.CreateListing -> CreateListingFlow(onJourneyEnd)
        is ActiveJourney.EditListing -> EditListingFlow(journey, onJourneyEnd)
        is ActiveJourney.DeleteListing -> DeleteListingFlow(journey, onJourneyEnd)
        is ActiveJourney.Checkout -> CheckoutFlow(onJourneyEnd)
        is ActiveJourney.Logout -> LogoutFlow(onJourneyEnd, onSignedOut)
    }
}

@Composable
private fun CreateListingFlow(onJourneyEnd: () -> Unit) {
    CreateListingJourneyHost(onFinish = onJourneyEnd) { view ->
        when (view) {
            is CreateListingView.EnterTitle ->
                CreateListingEnterTitleScreen(view.controller, onCancel = onJourneyEnd)
            is CreateListingView.EnterDescription ->
                CreateListingEnterDescriptionScreen(view.step, view.controller)
            is CreateListingView.EnterPrice ->
                CreateListingEnterPriceScreen(view.step, view.controller)
            is CreateListingView.Review ->
                CreateListingReviewScreen(view.step, view.controller)
            is CreateListingView.Published ->
                CreateListingPublishedScreen(view.step, view.controller)
        }
    }
}

@Composable
private fun EditListingFlow(journey: ActiveJourney.EditListing, onJourneyEnd: () -> Unit) {
    EditListingJourneyHost(onFinish = onJourneyEnd) { view ->
        when (view) {
            is EditListingView.EnterTitle ->
                EditListingEnterTitleScreen(journey.listingId, view.controller, onCancel = onJourneyEnd)
            is EditListingView.EnterDescription ->
                EditListingEnterDescriptionScreen(view.step, view.controller)
            is EditListingView.EnterPrice ->
                EditListingEnterPriceScreen(view.step, view.controller)
            is EditListingView.Done ->
                EditListingDoneScreen(view.controller)
        }
    }
}

@Composable
private fun DeleteListingFlow(journey: ActiveJourney.DeleteListing, onJourneyEnd: () -> Unit) {
    DeleteListingJourneyHost(onFinish = onJourneyEnd) { view ->
        when (view) {
            is DeleteListingView.Confirm ->
                DeleteListingConfirmScreen(journey.listingId, view.controller, onCancel = onJourneyEnd)
            is DeleteListingView.Done ->
                DeleteListingDoneScreen(view.controller)
        }
    }
}

@Composable
private fun CheckoutFlow(onJourneyEnd: () -> Unit) {
    CheckoutJourneyHost(onFinish = onJourneyEnd) { view ->
        when (view) {
            is CheckoutView.ReviewCart -> CheckoutReviewCartScreen(view.controller)
            is CheckoutView.EnterAddress -> CheckoutEnterAddressScreen(view.controller)
            is CheckoutView.SelectPayment -> CheckoutSelectPaymentScreen(view.step, view.controller)
            is CheckoutView.EnterCardDetails -> CheckoutEnterCardScreen(view.step, view.controller)
            is CheckoutView.Processing -> CheckoutProcessingScreen(view.step, view.controller)
            is CheckoutView.Done -> CheckoutDoneScreen(view.controller)
        }
    }
}

@Composable
private fun LogoutFlow(onJourneyEnd: () -> Unit, onSignedOut: () -> Unit) {
    LogoutJourneyHost(onFinish = onSignedOut) { view ->
        when (view) {
            is LogoutView.Confirm -> LogoutConfirmScreen(view.controller, onCancel = onJourneyEnd)
            is LogoutView.Done -> LogoutDoneScreen(view.controller)
        }
    }
}

@Composable
private fun MainScaffold(activeTab: MainTab, onTabSelected: (MainTab) -> Unit, actions: MainActions) {
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
                    onCreateListing = actions.onCreateListing,
                    onEditListing = actions.onEditListing,
                    onDeleteListing = actions.onDeleteListing,
                )
                MainTab.Cart -> CartTab(onCheckout = actions.onCheckout)
                MainTab.Profile -> ProfileTab(onLogout = actions.onLogout)
            }
        }
    }
}
