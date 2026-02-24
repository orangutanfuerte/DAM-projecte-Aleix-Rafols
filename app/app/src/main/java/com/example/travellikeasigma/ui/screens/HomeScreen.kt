package com.example.travellikeasigma.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.travellikeasigma.R
import com.example.travellikeasigma.model.Trip
import com.example.travellikeasigma.model.sampleTrips

// ---------------------------------------------------------------------------
// HomeScreen
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNewTripClick:   () -> Unit,
    onAvatarClick:    () -> Unit,
    onItineraryClick: () -> Unit = {},
    onPackingClick:   () -> Unit = {},
    onPhotosClick:    () -> Unit = {},
    onPlacesClick:    () -> Unit = {}
) {
    var tripIndex by rememberSaveable { mutableIntStateOf(1) }
    val trip  = sampleTrips[tripIndex]
    val total = sampleTrips.size

    Scaffold(
        topBar = {
            HomeTopBar(
                onNewTripClick = onNewTripClick,
                onAvatarClick  = onAvatarClick
            )
        },
        contentWindowInsets = WindowInsets(0), //  TREU DEPENDENCIES SI NO L'USES
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)          // ← handles top-bar + bottom-nav spacing
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text       = stringResource(R.string.home_greeting),
                style      = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text  = stringResource(R.string.home_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            TripNavigator(
                currentIndex = tripIndex,
                total        = total,
                onPrev       = { if (tripIndex > 0) tripIndex-- },
                onNext       = { if (tripIndex < total - 1) tripIndex++ },
                onUpcoming   = {
                    val idx = sampleTrips.indexOfFirst { it.isUpcoming }
                    if (idx >= 0) tripIndex = idx
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            TripHeroCard(trip = trip)

            Spacer(modifier = Modifier.height(16.dp))

            StatGrid(
                trip             = trip,
                onItineraryClick = onItineraryClick,
                onPhotosClick    = onPhotosClick,
                onPackingClick   = onPackingClick,
                onPlacesClick    = onPlacesClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text       = stringResource(R.string.home_upcoming_days).uppercase(),
                style      = MaterialTheme.typography.labelMedium,
                color      = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            DayCard("01", trip.day1Title, trip.day1Sub, onItineraryClick)
            DayCard("02", trip.day2Title, trip.day2Sub, onItineraryClick)
            DayCard("03", trip.day3Title, trip.day3Sub, onItineraryClick)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ---------------------------------------------------------------------------
// TopAppBar
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    onNewTripClick: () -> Unit,
    onAvatarClick:  () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text       = stringResource(R.string.app_name),
                style      = MaterialTheme.typography.titleMedium,
                color      = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        actions = {
            FilledTonalButton(
                onClick  = onNewTripClick,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(stringResource(R.string.home_new_trip))
            }

            Surface(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable(onClickLabel = stringResource(R.string.cd_avatar)) { onAvatarClick() },
                color    = MaterialTheme.colorScheme.primaryContainer,
                shape    = CircleShape
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text       = "S",
                        style      = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color      = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign  = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
        }
    )
}

// ---------------------------------------------------------------------------
// Trip navigator  ‹  [n / total]  ⚡ Upcoming  ›
// ---------------------------------------------------------------------------

@Composable
private fun TripNavigator(
    currentIndex: Int,
    total:        Int,
    onPrev:       () -> Unit,
    onNext:       () -> Unit,
    onUpcoming:   () -> Unit
) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        OutlinedIconButton(
            onClick  = onPrev,
            enabled  = currentIndex > 0,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(Icons.Filled.ChevronLeft, contentDescription = stringResource(R.string.cd_prev_trip))
        }

        Row(
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text  = "Your Trips",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text       = "${currentIndex + 1} / $total",
                    style      = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }

            FilledTonalButton(
                onClick          = onUpcoming,
                shape            = RoundedCornerShape(20.dp),
                modifier         = Modifier.height(32.dp),
                contentPadding   = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
            ) {
                Icon(Icons.Filled.ElectricBolt, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(stringResource(R.string.home_upcoming_pill), style = MaterialTheme.typography.labelSmall)
            }
        }

        OutlinedIconButton(
            onClick  = onNext,
            enabled  = currentIndex < total - 1,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(Icons.Filled.ChevronRight, contentDescription = stringResource(R.string.cd_next_trip))
        }
    }
}

// ---------------------------------------------------------------------------
// Hero card — unique color per trip, auto text contrast
// ---------------------------------------------------------------------------

@Composable
private fun TripHeroCard(trip: Trip) {
    val onHero = if (trip.heroColor.luminance() > 0.35f) Color.Black else Color.White

    Card(
        modifier  = Modifier.fillMaxWidth(),
        colors    = CardDefaults.cardColors(containerColor = trip.heroColor),
        shape     = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text       = trip.statusLabel.uppercase(),
                style      = MaterialTheme.typography.labelSmall,
                color      = onHero.copy(alpha = 0.70f),
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text       = trip.name,
                style      = MaterialTheme.typography.headlineMedium,
                color      = onHero,
                fontWeight = FontWeight.Bold
            )
            Text(
                text  = trip.dates,
                style = MaterialTheme.typography.bodySmall,
                color = onHero.copy(alpha = 0.75f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text  = stringResource(R.string.home_planning_progress),
                    style = MaterialTheme.typography.labelSmall,
                    color = onHero.copy(alpha = 0.70f)
                )
                Text(
                    text  = trip.progressLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = onHero.copy(alpha = 0.70f)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress   = { trip.progressFraction },
                modifier   = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color      = onHero,
                trackColor = onHero.copy(alpha = 0.25f)
            )
        }
    }
}

// ---------------------------------------------------------------------------
// 2x2 stat grid — tappable
// ---------------------------------------------------------------------------

private data class StatItem(
    val emoji:    String,
    val value:    String,
    val labelRes: Int,
    val onClick:  () -> Unit
)

@Composable
private fun StatGrid(
    trip:             Trip,
    onItineraryClick: () -> Unit,
    onPhotosClick:    () -> Unit,
    onPackingClick:   () -> Unit,
    onPlacesClick:    () -> Unit
) {
    val items = listOf(
        StatItem("🗓", trip.daysPlanned,   R.string.home_stat_days,    onItineraryClick),
        StatItem("📸", trip.photoCount,    R.string.home_stat_photos,  onPhotosClick),
        StatItem("🧳", trip.packingStatus, R.string.home_stat_packing, onPackingClick),
        StatItem("📍", trip.placesCount,   R.string.home_stat_places,  onPlacesClick)
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { item ->
                    StatCard(
                        emoji    = item.emoji,
                        value    = item.value,
                        label    = stringResource(item.labelRes),
                        onClick  = item.onClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    emoji:    String,
    value:    String,
    label:    String,
    onClick:  () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick   = onClick,
        modifier  = modifier,
        shape     = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = emoji, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

// ---------------------------------------------------------------------------
// Day row card — tappable → Itinerary
// ---------------------------------------------------------------------------

@Composable
private fun DayCard(
    day:      String,
    title:    String,
    subtitle: String,
    onClick:  () -> Unit
) {
    Card(
        onClick   = onClick,
        modifier  = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape     = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier          = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text       = day,
                style      = MaterialTheme.typography.headlineMedium,
                color      = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier   = Modifier.width(40.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(
                imageVector        = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                tint               = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier           = Modifier.size(14.dp)
            )
        }
    }
}