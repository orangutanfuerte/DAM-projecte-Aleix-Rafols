package com.example.travellikeasigma.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.travellikeasigma.R
import com.example.travellikeasigma.model.ActivityType
import com.example.travellikeasigma.model.DayWeather
import com.example.travellikeasigma.model.ItineraryActivity
import com.example.travellikeasigma.model.ItineraryDay
import com.example.travellikeasigma.model.WeatherType
import com.example.travellikeasigma.model.displayName
import com.example.travellikeasigma.model.sampleItinerary

// ---------------------------------------------------------------------------
// Tag colors (from HTML preview)
// ---------------------------------------------------------------------------

private val FoodTagBg = Color(0xFFFEF3E2)
private val FoodTagText = Color(0xFFC8773A)
private val SightTagBg = Color(0xFFE8F0E9)
private val SightTagText = Color(0xFF4A7C59)
private val TransitTagBg = Color(0xFFEAF0F8)
private val TransitTagText = Color(0xFF3A6AC8)

// ---------------------------------------------------------------------------
// ItineraryScreen
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryScreen(onAddActivityClick: (dayNumber: Int) -> Unit = {}) {
    var selectedDay by rememberSaveable { mutableIntStateOf(0) }
    val day = sampleItinerary[selectedDay]

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.itinerary_title),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.itinerary_subtitle),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddActivityClick(day.dayNumber) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.cd_add_activity)
                )
            }
        },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Day selector
            DaySelectorRow(
                days = sampleItinerary,
                selectedIndex = selectedDay,
                onDaySelected = { selectedDay = it }
            )

            // Weather card
            WeatherCard(weather = day.weather)

            // Date + city label
            Text(
                text = "${day.date} \u00b7 ${day.city}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Timeline
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            ) {
                itemsIndexed(day.activities) { index, activity ->
                    TimelineItem(
                        activity = activity,
                        isLast = index == day.activities.lastIndex
                    )
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Day selector — horizontal scrollable row of pill chips
// ---------------------------------------------------------------------------

@Composable
private fun DaySelectorRow(
    days: List<ItineraryDay>,
    selectedIndex: Int,
    onDaySelected: (Int) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 12.dp)
    ) {
        itemsIndexed(days) { index, day ->
            FilterChip(
                selected = index == selectedIndex,
                onClick = { onDaySelected(index) },
                label = {
                    Text(
                        text = "Day ${day.dayNumber}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (index == selectedIndex) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                shape = RoundedCornerShape(20.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

// ---------------------------------------------------------------------------
// Weather card
// ---------------------------------------------------------------------------

@Composable
private fun WeatherCard(weather: DayWeather) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = when (weather.icon) {
                    WeatherType.SUNNY -> Icons.Filled.WbSunny
                    WeatherType.PARTLY_CLOUDY -> Icons.Filled.WbCloudy
                    WeatherType.CLOUDY -> Icons.Filled.Cloud
                    WeatherType.RAINY -> Icons.Filled.Grain
                },
                contentDescription = weather.description,
                modifier = Modifier.size(28.dp),
                tint = when (weather.icon) {
                    WeatherType.SUNNY -> Color(0xFFFF9800)
                    WeatherType.PARTLY_CLOUDY -> Color(0xFF78909C)
                    WeatherType.CLOUDY -> Color(0xFF90A4AE)
                    WeatherType.RAINY -> Color(0xFF42A5F5)
                }
            )
            Column {
                Text(
                    text = "${weather.tempLow}\u2013${weather.tempHigh}\u00b0C",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = weather.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Timeline item — dot + line on the left, card on the right
// ---------------------------------------------------------------------------

@Composable
private fun TimelineItem(
    activity: ItineraryActivity,
    isLast: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Left column: time + dot + line
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(40.dp)
        ) {
            Text(
                text = activity.time,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.outlineVariant)
                )
            }
        }

        // Right column: event card
        Card(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 10.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = activity.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = activity.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 2.dp)
                )
                if (activity.cost > 0.0) {
                    Text(
                        text = "%.2f\u00a0\u20ac".format(activity.cost),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                activity.tag?.let { tag ->
                    Spacer(modifier = Modifier.height(8.dp))
                    ActivityTag(tag)
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// Activity tag chip
// ---------------------------------------------------------------------------

@Composable
private fun ActivityTag(type: ActivityType) {
    val (bg, textColor) = when (type) {
        ActivityType.FOOD -> FoodTagBg to FoodTagText
        ActivityType.SIGHTSEEING -> SightTagBg to SightTagText
        ActivityType.TRANSIT -> TransitTagBg to TransitTagText
        ActivityType.OTHERS -> return
    }

    Text(
        text = type.displayName(),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.SemiBold,
        color = textColor,
        modifier = Modifier
            .background(bg, RoundedCornerShape(20.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}
