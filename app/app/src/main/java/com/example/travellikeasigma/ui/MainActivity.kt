package com.example.travellikeasigma

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.travellikeasigma.ui.navigation.NavGraph
import com.example.travellikeasigma.ui.navigation.Routes
import com.example.travellikeasigma.ui.components.BottomNavBar
import com.example.travellikeasigma.ui.theme.TravelLikeASigmaTheme
import com.example.travellikeasigma.ui.viewmodels.AuthViewModel
import com.example.travellikeasigma.ui.viewmodels.PreferencesViewModel
import com.example.travellikeasigma.ui.viewmodels.TripViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("travel_sigma_prefs", Context.MODE_PRIVATE)
        if (prefs.contains("language")) {
            val lang = prefs.getString("language", "en") ?: "en"
            val locale = Locale(lang)
            val config = Configuration(newBase.resources.configuration)
            config.setLocale(locale)
            super.attachBaseContext(newBase.createConfigurationContext(config))
        } else {
            super.attachBaseContext(newBase)
        }
    }

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
            TravelSigmaApp(onRecreate = { recreate() })
        }
    }
}

// ---------------------------------------------------------------------------
// Root composable — owns the NavController and the outer Scaffold
// ---------------------------------------------------------------------------
@Composable
fun TravelSigmaApp(onRecreate: () -> Unit = {}) {
    val navController        = rememberNavController()
    val tripViewModel:        TripViewModel        = hiltViewModel()
    val authViewModel:        AuthViewModel        = hiltViewModel()
    val preferencesViewModel: PreferencesViewModel = hiltViewModel()
    val snackbarHostState    = remember { SnackbarHostState() }

    // Theme driven by PreferencesViewModel (persisted in SharedPreferences)
    TravelLikeASigmaTheme(themeMode = preferencesViewModel.themeMode) {

        // Only show the bottom bar on the 5 main tab screens and only when there are trips
        val bottomBarRoutes = setOf(
            Routes.HOME,
            Routes.ITINERARY,
            "${Routes.ITINERARY}?day={day}",
            Routes.PHOTOS,
            Routes.PLACES
        )
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute   = backStackEntry?.destination?.route
        val showBottomBar  = currentRoute in bottomBarRoutes && tripViewModel.trips.isNotEmpty()

        Scaffold(
            contentWindowInsets = WindowInsets(0),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                if (showBottomBar) {
                    BottomNavBar(navController = navController)
                }
            }
        ) { innerPadding ->
            NavGraph(
                navController        = navController,
                tripViewModel        = tripViewModel,
                authViewModel        = authViewModel,
                preferencesViewModel = preferencesViewModel,
                snackbarHostState    = snackbarHostState,
                onRecreate           = onRecreate,
                modifier             = Modifier.padding(innerPadding)
            )
        }
    }
}
