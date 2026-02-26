package com.example.travellikeasigma.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.travellikeasigma.R
import com.example.travellikeasigma.ui.components.PlaceholderScreen

// ---------------------------------------------------------------------------
// Each of these is a one-liner today.
// When you're ready to build a screen out, just replace its body.
// ---------------------------------------------------------------------------

@Composable
fun PackingScreen() {
    PlaceholderScreen(
        title    = stringResource(R.string.packing_title),
        subtitle = stringResource(R.string.packing_subtitle)
    )
}

@Composable
fun PhotosScreen() {
    PlaceholderScreen(
        title    = stringResource(R.string.photos_title),
        subtitle = stringResource(R.string.photos_subtitle)
    )
}

@Composable
fun PlacesScreen() {
    PlaceholderScreen(
        title    = stringResource(R.string.places_title),
        subtitle = stringResource(R.string.places_subtitle)
    )
}

@Composable
fun NewTripScreen(onBackClick: () -> Unit) {
    // Will become the multi-step new-trip flow in a future session
    PlaceholderScreen(
        title    = stringResource(R.string.new_trip_title),
        subtitle = stringResource(R.string.new_trip_step_destination_sub)
    )
}
