package com.example.travellikeasigma.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.travellikeasigma.navigation.BottomNavDestination
import com.example.travellikeasigma.navigation.Routes

@Composable
fun BottomNavBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar (
        //windowInsets = WindowInsets(0),
        // modifier = Modifier.padding(bottom = 0.dp)
    ) {
        BottomNavDestination.all.forEach { destination ->
            NavigationBarItem(
                selected = currentRoute == destination.route,
                onClick  = {
                    navController.navigate(destination.route) {
                        // Popping up to HOME clears any screens that were pushed
                        // on top (e.g. from a card click), so the back stack is
                        // always clean when switching tabs.
                        popUpTo(Routes.HOME)
                        // Don't create a new copy if already on this tab
                        launchSingleTop = true
                    }
                },
                icon  = { Icon(imageVector = destination.icon, contentDescription = null) },
                label = { Text(stringResource(destination.labelRes)) }
            )
        }
    }
}