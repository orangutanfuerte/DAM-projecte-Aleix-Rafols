package com.example.travellikeasigma.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.travellikeasigma.model.sampleTrips
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
//   • always starts fresh (no saved scroll / state)
// ---------------------------------------------------------------------------
private fun NavHostController.navigateToTab(route: String) {
    navigate(route) {
        popUpTo(Routes.HOME)
        launchSingleTop = true
    }
}

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier:      Modifier = Modifier
) {
    val currentTrip = sampleTrips[0]

    NavHost(
        navController      = navController,
        startDestination   = Routes.HOME,
        modifier           = modifier,
        enterTransition    = { EnterTransition.None },
        exitTransition     = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition  = { ExitTransition.None }
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                trip             = currentTrip,
                onNewTripClick   = { navController.navigate(Routes.NEW_TRIP) },
                onAvatarClick    = { navController.navigate(Routes.PREFERENCES) },
                onItineraryClick = { navController.navigateToTab(Routes.ITINERARY) },
                onPackingClick   = { navController.navigateToTab(Routes.PACKING) },
                onPhotosClick    = { navController.navigateToTab(Routes.PHOTOS) },
                onPlacesClick    = { navController.navigateToTab(Routes.PLACES) },
                onDayClick       = { dayIndex ->
                    navController.navigateToTab(Routes.itineraryDay(dayIndex))
                }
            )
        }
        composable(
            route = "${Routes.ITINERARY}?day={day}",
            arguments = listOf(navArgument("day") { defaultValue = -1; type = NavType.IntType })
        ) { backStackEntry ->
            val initialDay = backStackEntry.arguments?.getInt("day") ?: -1
            ItineraryScreen(
                trip = currentTrip,
                initialDay = initialDay,
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
            PackingScreen(trip = currentTrip)
        }
        composable(Routes.PHOTOS) {
            PhotosScreen(trip = currentTrip)
        }
        composable(Routes.PLACES) {
            PlacesScreen(trip = currentTrip)
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
            NewTripScreen(
                onBackClick = { navController.popBackStack() },
                onSave      = { navController.popBackStack() }
            )
        }
    }
}
