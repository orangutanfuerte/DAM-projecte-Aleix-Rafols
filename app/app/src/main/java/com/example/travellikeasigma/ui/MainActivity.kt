package com.example.travellikeasigma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.toMutableStateList
import com.example.travellikeasigma.model.sampleUser
import com.example.travellikeasigma.navigation.NavGraph
import com.example.travellikeasigma.navigation.Routes
import com.example.travellikeasigma.ui.components.BottomNavBar
import com.example.travellikeasigma.ui.theme.LocalThemeMode
import com.example.travellikeasigma.ui.theme.ThemeMode
import com.example.travellikeasigma.ui.theme.TravelLikeASigmaTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var isChecking = true
        lifecycleScope.launch {
            delay(3000L)
            isChecking = false
        }
        installSplashScreen().apply {
            setKeepOnScreenCondition { isChecking }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeModeState = remember { mutableStateOf(ThemeMode.SYSTEM) }
            CompositionLocalProvider(LocalThemeMode provides themeModeState) {
                TravelLikeASigmaTheme(themeMode = themeModeState.value) {
                    TravelSigmaApp()
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Root composable — owns the NavController and the outer Scaffold
// ---------------------------------------------------------------------------
@Composable
fun TravelSigmaApp() {
    val navController = rememberNavController()
    val userTrips = remember { sampleUser.trips.toMutableStateList() }

    // Only show the bottom bar on the 5 main tab screens and only when there are trips
    val bottomBarRoutes = setOf(
        Routes.HOME,
        Routes.ITINERARY,
        "${Routes.ITINERARY}?day={day}",
        Routes.PACKING,
        Routes.PHOTOS,
        Routes.PLACES
    )
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute   = backStackEntry?.destination?.route
    val showBottomBar  = currentRoute in bottomBarRoutes && userTrips.isNotEmpty()

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            userTrips     = userTrips,
            modifier      = Modifier.padding(innerPadding)
        )
    }
}
