package com.example.travellikeasigma.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.travellikeasigma.ui.screen.AboutScreen
import com.example.travellikeasigma.ui.screen.AddActivityScreen
import com.example.travellikeasigma.ui.screen.HomeScreen
import com.example.travellikeasigma.ui.screen.ItineraryScreen
import com.example.travellikeasigma.ui.screen.NewTripScreen
import com.example.travellikeasigma.ui.screen.PackingScreen
import com.example.travellikeasigma.ui.screen.PhotosScreen
import com.example.travellikeasigma.ui.screen.PlacesScreen
import com.example.travellikeasigma.ui.screen.PreferencesScreen
import com.example.travellikeasigma.ui.screen.TermsScreen

// ---------------------------------------------------------------------------
// Helper — navigate to a bottom-nav tab from anywhere (card clicks, etc.)
// Behaves exactly like pressing the tab in the bottom bar:
//   • pops back to HOME so the stack stays flat
//   • won't duplicate the destination if already there
//   • restores saved scroll / state
// ---------------------------------------------------------------------------
private fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(Routes.HOME) {
            saveState = true
        }
        launchSingleTop = true
        restoreState    = true
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier:      Modifier = Modifier
) {
    NavHost(
        navController    = navController,
        startDestination = Routes.HOME,
        modifier         = modifier
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onNewTripClick   = { navController.navigate(Routes.NEW_TRIP) },
                onAvatarClick    = { navController.navigate(Routes.PREFERENCES) },
                // ✅ Card/day clicks use navigateToTab — same behaviour as the bar
                onItineraryClick = { navController.navigateToTab(Routes.ITINERARY) },
                onPackingClick   = { navController.navigateToTab(Routes.PACKING) },
                onPhotosClick    = { navController.navigateToTab(Routes.PHOTOS) },
                onPlacesClick    = { navController.navigateToTab(Routes.PLACES) }
            )
        }
        composable(Routes.ITINERARY) {
            ItineraryScreen(
                onAddActivityClick = { dayNumber ->
                    navController.navigate(Routes.addActivity(dayNumber))
                }
            )
        }
        composable(
            route = Routes.ADD_ACTIVITY,
            arguments = listOf(navArgument("dayNumber") { type = NavType.IntType })
        ) { backStackEntry ->
            val dayNumber = backStackEntry.arguments?.getInt("dayNumber") ?: 1
            AddActivityScreen(
                dayNumber = dayNumber,
                onBackClick = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }
        composable(Routes.PACKING) {
            PackingScreen()
        }
        composable(Routes.PHOTOS) {
            PhotosScreen()
        }
        composable(Routes.PLACES) {
            PlacesScreen()
        }
        composable(Routes.PREFERENCES) {
            PreferencesScreen(
                onBackClick  = { navController.popBackStack() },
                onTermsClick = { navController.navigate(Routes.TERMS) },
                onAboutClick = { navController.navigate(Routes.ABOUT) }
            )
        }
        composable(Routes.TERMS) {
            TermsScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.ABOUT) {
            AboutScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.NEW_TRIP) {
            NewTripScreen(onBackClick = { navController.popBackStack() })
        }
    }
}