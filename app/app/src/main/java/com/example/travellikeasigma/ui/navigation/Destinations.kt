package com.example.travellikeasigma.navigation

// ---------------------------------------------------------------------------
// Bottom-nav destinations — only the 5 tabs that appear in the BottomNavBar
// ---------------------------------------------------------------------------
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.travellikeasigma.R

// ---------------------------------------------------------------------------
// Routes — one object to hold all route strings, avoiding hardcoded literals
// ---------------------------------------------------------------------------
object Routes {
    const val HOME          = "home"
    const val ITINERARY     = "itinerary"
    const val PACKING       = "packing"
    const val PHOTOS        = "photos"
    const val PLACES        = "places"
    const val PREFERENCES   = "preferences"
    const val TERMS         = "terms"
    const val ABOUT         = "about"
    const val NEW_TRIP      = "new_trip"
    const val ADD_ACTIVITY  = "add_activity/{dayNumber}"
    fun addActivity(dayNumber: Int): String = "add_activity/$dayNumber"
    fun itineraryDay(dayIndex: Int): String = "$ITINERARY?day=$dayIndex"
}

sealed class BottomNavDestination(
    val route: String,
    val icon: ImageVector,
    @StringRes val labelRes: Int
) {
    data object Home : BottomNavDestination(Routes.HOME, Icons.Filled.Home, R.string.nav_home)
    data object Itinerary : BottomNavDestination(Routes.ITINERARY, Icons.Filled.CalendarMonth, R.string.nav_itinerary)
    data object Packing : BottomNavDestination(Routes.PACKING, Icons.Filled.CheckBox, R.string.nav_packing)
    data object Photos : BottomNavDestination(Routes.PHOTOS, Icons.Filled.Photo, R.string.nav_photos)
    data object Places : BottomNavDestination(Routes.PLACES, Icons.Filled.Place, R.string.nav_places)

    companion object {
        val all = listOf(Home, Itinerary, Packing, Photos, Places)
    }
}
