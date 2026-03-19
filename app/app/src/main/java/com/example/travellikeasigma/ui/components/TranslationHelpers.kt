package com.example.travellikeasigma.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.travellikeasigma.R
import com.example.travellikeasigma.domain.ActivityType
import com.example.travellikeasigma.domain.Destination
import com.example.travellikeasigma.domain.TripStatus

@Composable
fun TripStatus.label(): String = when (this) {
    TripStatus.UPCOMING -> stringResource(R.string.trip_status_upcoming)
    TripStatus.ACTIVE   -> stringResource(R.string.trip_status_active)
    TripStatus.PAST     -> stringResource(R.string.trip_status_past)
}

@Composable
fun ActivityType.label(): String = when (this) {
    ActivityType.FOOD        -> stringResource(R.string.activity_type_food)
    ActivityType.SIGHTSEEING -> stringResource(R.string.activity_type_sightseeing)
    ActivityType.TRANSIT     -> stringResource(R.string.activity_type_transit)
    ActivityType.OTHERS      -> stringResource(R.string.activity_type_others)
}

@Composable
fun Destination.translatedName(): String = when (destinationName) {
    "Japan"   -> stringResource(R.string.destination_japan)
    "Iceland" -> stringResource(R.string.destination_iceland)
    "Italy"   -> stringResource(R.string.destination_italy)
    "Spain"   -> stringResource(R.string.destination_spain)
    "Brasil"  -> stringResource(R.string.destination_brasil)
    else      -> destinationName
}
