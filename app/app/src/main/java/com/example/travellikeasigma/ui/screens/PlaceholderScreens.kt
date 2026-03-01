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
fun PlacesScreen() {
    PlaceholderScreen(
        title    = stringResource(R.string.places_title),
        subtitle = stringResource(R.string.places_subtitle)
    )
}
