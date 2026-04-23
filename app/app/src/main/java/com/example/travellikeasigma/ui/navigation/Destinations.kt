package com.example.travellikeasigma.ui.navigation

// ---------------------------------------------------------------------------
// Bottom-nav destinations — only the 4 tabs that appear in the BottomNavBar
// ---------------------------------------------------------------------------
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.travellikeasigma.R

// ---------------------------------------------------------------------------
// Routes — one object to hold all route strings, avoiding hardcoded literals
// ---------------------------------------------------------------------------
object Routes {
    const val LOGIN         = "login"
    const val REGISTER      = "register"
    const val HOME          = "home"
    const val ITINERARY     = "itinerary"
    const val PHOTOS        = "photos"
    const val PLACES        = "places"
    const val PREFERENCES   = "preferences"
    const val PROFILE       = "profile"
    const val TERMS         = "terms"
    const val ABOUT         = "about"
    const val NEW_TRIP      = "new_trip"
    const val ADD_ACTIVITY  = "add_activity/{dayNumber}"
    fun addActivity(dayNumber: Int): String = "add_activity/$dayNumber"
    const val EDIT_ACTIVITY = "edit_activity/{dayNumber}/{activityId}"
    fun editActivity(dayNumber: Int, activityId: Int): String = "edit_activity/$dayNumber/$activityId"
    fun itineraryDay(dayIndex: Int): String = "$ITINERARY?day=$dayIndex"
}

sealed class BottomNavDestination(
    val route: String,
    val icon: ImageVector,
    @StringRes val labelRes: Int
) {
    data object Home : BottomNavDestination(Routes.HOME, Icons.Filled.Home, R.string.nav_home)
    data object Itinerary : BottomNavDestination(Routes.ITINERARY, Icons.Filled.CalendarMonth, R.string.nav_itinerary)
    data object Photos : BottomNavDestination(Routes.PHOTOS, Icons.Filled.Photo, R.string.nav_photos)
    data object Places : BottomNavDestination(Routes.PLACES, Icons.Filled.Place, R.string.nav_places)

    companion object {
        val all = listOf(Home, Itinerary, Photos, Places)
    }
}
