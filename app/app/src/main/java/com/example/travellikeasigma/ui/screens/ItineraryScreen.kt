package com.example.travellikeasigma.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.example.travellikeasigma.R
import com.example.travellikeasigma.model.ActivityType
import com.example.travellikeasigma.model.ItineraryActivity
import com.example.travellikeasigma.model.Trip
import com.example.travellikeasigma.model.displayName
import com.example.travellikeasigma.ui.theme.FoodTagBackground
import com.example.travellikeasigma.ui.theme.FoodTagText
import com.example.travellikeasigma.ui.theme.SightseeingTagBackground
import com.example.travellikeasigma.ui.theme.SightseeingTagText
import com.example.travellikeasigma.ui.theme.TransitTagBackground
import com.example.travellikeasigma.ui.theme.TransitTagText
import com.example.travellikeasigma.ui.theme.WeatherCloudyTint
import com.example.travellikeasigma.ui.theme.WeatherPartlyCloudyTint
import com.example.travellikeasigma.ui.theme.WeatherRainyTint
import com.example.travellikeasigma.ui.components.TripTopAppBar
import com.example.travellikeasigma.ui.theme.WeatherSunnyTint

// ---------------------------------------------------------------------------
// ItineraryScreen
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryScreen(
    trip: Trip,
    initialDay: Int = -1,
    onAddActivityClick: (dayNumber: Int) -> Unit = {},
    onEditActivityClick: (dayNumber: Int, activityId: Int) -> Unit = { _, _ -> }
) {
    var selectedDay by rememberSaveable {
        mutableIntStateOf(if (initialDay >= 0) initialDay else 0)
    }
    val dayNumber = selectedDay + 1
    val dayActivities = trip.getActivitiesByDay(dayNumber)

    Scaffold(
        topBar = {
            TripTopAppBar(
                title = stringResource(R.string.itinerary_title),
                subtitle = stringResource(R.string.itinerary_subtitle, trip.name, trip.daysCount),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddActivityClick(dayNumber) },
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
                daysCount = trip.daysCount,
                selectedIndex = selectedDay,
                onDaySelected = { selectedDay = it }
            )

            // Weather card
            val weather = remember(selectedDay) {
                val type = WeatherType.entries.random()
                val description = when (type) {
                    WeatherType.SUNNY         -> "Sunny"
                    WeatherType.PARTLY_CLOUDY -> "Partly Cloudy"
                    WeatherType.CLOUDY        -> "Cloudy"
                    WeatherType.RAINY         -> "Rainy"
                }
                DayWeather(type, (18..28).random(), (10..17).random(), description)
            }
            WeatherCard(weather = weather)

            // Date + city label
            Text(
                text = trip.getDateForDay(dayNumber).format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Timeline
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            ) {
                val sortedActivities = dayActivities
                itemsIndexed(sortedActivities) { index, activity ->
                    TimelineItem(
                        activity = activity,
                        isLast = index == sortedActivities.lastIndex,
                        onLongPress = { onEditActivityClick(dayNumber, activity.id) }
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
    daysCount: Int,
    selectedIndex: Int,
    onDaySelected: (Int) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(bottom = 12.dp)
    ) {
        items(daysCount) { index ->
            FilterChip(
                selected = index == selectedIndex,
                onClick = { onDaySelected(index) },
                label = {
                    Text(
                        text = "Day ${index + 1}",
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
enum class WeatherType { SUNNY, PARTLY_CLOUDY, CLOUDY, RAINY }

data class DayWeather(
    val icon: WeatherType,
    val tempHigh: Int,
    val tempLow: Int,
    val description: String
)

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
                    WeatherType.SUNNY -> WeatherSunnyTint
                    WeatherType.PARTLY_CLOUDY -> WeatherPartlyCloudyTint
                    WeatherType.CLOUDY -> WeatherCloudyTint
                    WeatherType.RAINY -> WeatherRainyTint
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TimelineItem(
    activity: ItineraryActivity,
    isLast: Boolean,
    onLongPress: () -> Unit = {}
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
                .padding(bottom = 10.dp)
                .combinedClickable(
                    onClick = {},
                    onLongClick = onLongPress
                ),
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
        ActivityType.FOOD -> FoodTagBackground to FoodTagText
        ActivityType.SIGHTSEEING -> SightseeingTagBackground to SightseeingTagText
        ActivityType.TRANSIT -> TransitTagBackground to TransitTagText
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
