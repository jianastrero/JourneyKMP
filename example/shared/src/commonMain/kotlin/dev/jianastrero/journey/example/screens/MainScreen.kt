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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import dev.jianastrero.journey.example.LocalAppViewModel
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

private sealed interface MainDest {
    sealed interface Tab : MainDest {
        data object Home : Tab
        data object Listings : Tab
        data object Cart : Tab
        data object Profile : Tab
    }
    data object CreateListing : MainDest
    data class EditListing(val id: String) : MainDest
    data class DeleteListing(val id: String) : MainDest
    data object Checkout : MainDest
    data object Logout : MainDest
}

private data class MainScaffoldActions(
    val onCreateListing: () -> Unit,
    val onEditListing: (String) -> Unit,
    val onDeleteListing: (String) -> Unit,
    val onCheckout: () -> Unit,
    val onLogout: () -> Unit,
)

private val mainDestSaver = listSaver<SnapshotStateList<Any>, String>(
    save = { list ->
        list.map { dest ->
            when (dest) {
                is MainDest.Tab.Home -> "Home"
                is MainDest.Tab.Listings -> "Listings"
                is MainDest.Tab.Cart -> "Cart"
                is MainDest.Tab.Profile -> "Profile"
                is MainDest.CreateListing -> "CreateListing"
                is MainDest.EditListing -> "EditListing|${dest.id}"
                is MainDest.DeleteListing -> "DeleteListing|${dest.id}"
                is MainDest.Checkout -> "Checkout"
                is MainDest.Logout -> "Logout"
                else -> ""
            }
        }
    },
    restore = { saved ->
        val list = mutableStateListOf<Any>()
        saved.forEach { encoded ->
            val parts = encoded.split("|")
            when (parts.getOrNull(0)) {
                "Home" -> list.add(MainDest.Tab.Home)
                "Listings" -> list.add(MainDest.Tab.Listings)
                "Cart" -> list.add(MainDest.Tab.Cart)
                "Profile" -> list.add(MainDest.Tab.Profile)
                "CreateListing" -> list.add(MainDest.CreateListing)
                "EditListing" -> list.add(MainDest.EditListing(parts[1]))
                "DeleteListing" -> list.add(MainDest.DeleteListing(parts[1]))
                "Checkout" -> list.add(MainDest.Checkout)
                "Logout" -> list.add(MainDest.Logout)
            }
        }
        list
    }
)

@Composable
internal fun MainScreen(onSignedOut: () -> Unit) {
    val backStack = rememberSaveable(saver = mainDestSaver) { mutableStateListOf(MainDest.Tab.Home) }
    val onJourneyEnd: () -> Unit = { backStack.removeLastOrNull() }

    NavDisplay(backStack = backStack, onBack = { backStack.removeLastOrNull() }) { dest ->
        when (dest) {
            is MainDest.Tab -> NavEntry(dest) {
                val vm = LocalAppViewModel.current
                MainScaffold(
                    activeTab = dest,
                    onTabSelected = { tab -> backStack[backStack.lastIndex] = tab },
                    actions = MainScaffoldActions(
                        onCreateListing = { backStack.add(MainDest.CreateListing) },
                        onEditListing = { id ->
                            vm.editingListingId = id
                            backStack.add(MainDest.EditListing(id))
                        },
                        onDeleteListing = { id ->
                            vm.deletingListingId = id
                            backStack.add(MainDest.DeleteListing(id))
                        },
                        onCheckout = { backStack.add(MainDest.Checkout) },
                        onLogout = { backStack.add(MainDest.Logout) },
                    ),
                )
            }
            is MainDest.CreateListing -> NavEntry(dest) { CreateListingFlow(onJourneyEnd) }
            is MainDest.EditListing -> NavEntry(dest) { EditListingFlow(dest.id, onJourneyEnd) }
            is MainDest.DeleteListing -> NavEntry(dest) { DeleteListingFlow(dest.id, onJourneyEnd) }
            is MainDest.Checkout -> NavEntry(dest) { CheckoutFlow(onJourneyEnd) }
            is MainDest.Logout -> NavEntry(dest) { LogoutFlow(onJourneyEnd, onSignedOut) }
            else -> NavEntry(dest) {}
        }
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
private fun EditListingFlow(listingId: String, onJourneyEnd: () -> Unit) {
    EditListingJourneyHost(onFinish = onJourneyEnd) { view ->
        when (view) {
            is EditListingView.EnterTitle ->
                EditListingEnterTitleScreen(listingId, view.controller, onCancel = onJourneyEnd)
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
private fun DeleteListingFlow(listingId: String, onJourneyEnd: () -> Unit) {
    DeleteListingJourneyHost(onFinish = onJourneyEnd) { view ->
        when (view) {
            is DeleteListingView.Confirm ->
                DeleteListingConfirmScreen(listingId, view.controller, onCancel = onJourneyEnd)
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
private fun MainScaffold(
    activeTab: MainDest.Tab,
    onTabSelected: (MainDest.Tab) -> Unit,
    actions: MainScaffoldActions,
) {
    val vm = LocalAppViewModel.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = activeTab == MainDest.Tab.Home,
                    onClick = { onTabSelected(MainDest.Tab.Home) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                )
                NavigationBarItem(
                    selected = activeTab == MainDest.Tab.Listings,
                    onClick = { onTabSelected(MainDest.Tab.Listings) },
                    icon = { Icon(Icons.Default.Store, contentDescription = "My Listings") },
                    label = { Text("Listings") },
                )
                NavigationBarItem(
                    selected = activeTab == MainDest.Tab.Cart,
                    onClick = { onTabSelected(MainDest.Tab.Cart) },
                    icon = {
                        val count = vm.cartItemCount
                        BadgedBox(badge = { if (count > 0) Badge { Text("$count") } }) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                        }
                    },
                    label = { Text("Cart") },
                )
                NavigationBarItem(
                    selected = activeTab == MainDest.Tab.Profile,
                    onClick = { onTabSelected(MainDest.Tab.Profile) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                )
            }
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(bottom = padding.calculateBottomPadding())) {
            when (activeTab) {
                MainDest.Tab.Home -> HomeTab(onAddToCart = { listingId ->
                    vm.listings.firstOrNull { it.id == listingId }?.let { vm.addToCart(it) }
                })
                MainDest.Tab.Listings -> MyListingsTab(
                    onCreateListing = actions.onCreateListing,
                    onEditListing = actions.onEditListing,
                    onDeleteListing = actions.onDeleteListing,
                )
                MainDest.Tab.Cart -> CartTab(onCheckout = actions.onCheckout)
                MainDest.Tab.Profile -> ProfileTab(onLogout = actions.onLogout)
            }
        }
    }
}
