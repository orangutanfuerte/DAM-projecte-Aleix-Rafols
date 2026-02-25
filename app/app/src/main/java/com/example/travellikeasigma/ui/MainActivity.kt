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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.travellikeasigma.navigation.NavGraph
import com.example.travellikeasigma.navigation.Routes
import com.example.travellikeasigma.ui.components.BottomNavBar
import com.example.travellikeasigma.ui.theme.TravelLikeASigmaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TravelLikeASigmaTheme {
                TravelSigmaApp()
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

    // Only show the bottom bar on the 5 main tab screens
    val bottomBarRoutes = setOf(
        Routes.HOME,
        Routes.ITINERARY,
        Routes.PACKING,
        Routes.PHOTOS,
        Routes.PLACES
    )
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute   = backStackEntry?.destination?.route
    val showBottomBar  = currentRoute in bottomBarRoutes

    Scaffold(
        contentWindowInsets = WindowInsets(0), //  TREU DEPENDENCIES SI NO L'USES
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(navController = navController)

            }
        }//, contentWindowInsets = WindowInsets.safeDrawing //  TREU DEPENDENCIES SI NO L'USES
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
