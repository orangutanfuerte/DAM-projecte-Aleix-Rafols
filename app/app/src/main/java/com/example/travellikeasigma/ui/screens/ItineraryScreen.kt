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

// ---------------------------------------------------------------------------
// Data models
// ---------------------------------------------------------------------------

enum class ActivityType { FOOD, SIGHTSEEING, TRANSIT }

enum class WeatherType { SUNNY, PARTLY_CLOUDY, CLOUDY, RAINY }

data class ItineraryActivity(
    val time: String,
    val title: String,
    val subtitle: String,
    val tag: ActivityType? = null
)

data class DayWeather(
    val icon: WeatherType,
    val tempHigh: Int,
    val tempLow: Int,
    val description: String
)

data class ItineraryDay(
    val dayNumber: Int,
    val date: String,
    val city: String,
    val weather: DayWeather,
    val activities: List<ItineraryActivity>
)

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
// Sample data — 7 days of Japan Highlights
// ---------------------------------------------------------------------------

private val sampleItinerary = listOf(
    ItineraryDay(
        dayNumber = 1, date = "Mar 14, 2025", city = "Tokyo",
        weather = DayWeather(WeatherType.PARTLY_CLOUDY, 18, 12, "Partly cloudy"),
        activities = listOf(
            ItineraryActivity("10:00", "Arrive at Narita Airport", "Flight JL081 \u00b7 Terminal 2", ActivityType.TRANSIT),
            ItineraryActivity("12:30", "Narita Express \u2192 Shinjuku", "~90 min \u00b7 \u00a53,070", ActivityType.TRANSIT),
            ItineraryActivity("14:00", "Hotel Check-in", "Shinjuku Granbell Hotel"),
            ItineraryActivity("16:00", "Shinjuku Gyoen Garden", "National garden \u00b7 \u00a5500", ActivityType.SIGHTSEEING),
            ItineraryActivity("19:00", "Dinner at Fuunji Ramen", "Tsukemen \u00b7 ~\u00a51,200", ActivityType.FOOD)
        )
    ),
    ItineraryDay(
        dayNumber = 2, date = "Mar 15, 2025", city = "Tokyo",
        weather = DayWeather(WeatherType.SUNNY, 20, 11, "Clear skies"),
        activities = listOf(
            ItineraryActivity("09:00", "Meiji Shrine", "Harajuku \u00b7 Free entry", ActivityType.SIGHTSEEING),
            ItineraryActivity("11:00", "Takeshita Street", "Shopping \u00b7 Harajuku", ActivityType.SIGHTSEEING),
            ItineraryActivity("13:00", "Lunch at Afuri Ramen", "Yuzu shio ramen \u00b7 ~\u00a51,000", ActivityType.FOOD),
            ItineraryActivity("15:00", "Shibuya Crossing", "Iconic scramble crossing", ActivityType.SIGHTSEEING),
            ItineraryActivity("19:30", "Dinner in Shibuya", "Izakaya \u00b7 ~\u00a53,500", ActivityType.FOOD)
        )
    ),
    ItineraryDay(
        dayNumber = 3, date = "Mar 16, 2025", city = "Nikko",
        weather = DayWeather(WeatherType.CLOUDY, 14, 6, "Overcast"),
        activities = listOf(
            ItineraryActivity("08:00", "Shinkansen to Nikko", "~2 hrs \u00b7 JR Pass", ActivityType.TRANSIT),
            ItineraryActivity("10:30", "T\u014dsh\u014d-g\u016b Shrine", "UNESCO World Heritage", ActivityType.SIGHTSEEING),
            ItineraryActivity("12:30", "Yuba Lunch", "Local tofu-skin specialty", ActivityType.FOOD),
            ItineraryActivity("14:00", "Keg\u014dn Falls", "97m waterfall \u00b7 \u00a5570 elevator", ActivityType.SIGHTSEEING),
            ItineraryActivity("17:00", "Return to Tokyo", "Shinkansen \u00b7 ~2 hrs", ActivityType.TRANSIT)
        )
    ),
    ItineraryDay(
        dayNumber = 4, date = "Mar 17, 2025", city = "Tokyo",
        weather = DayWeather(WeatherType.SUNNY, 19, 10, "Sunny"),
        activities = listOf(
            ItineraryActivity("09:00", "Tsukiji Outer Market", "Street food \u00b7 Fresh seafood", ActivityType.FOOD),
            ItineraryActivity("11:30", "teamLab Borderless", "Digital art museum \u00b7 \u00a53,800", ActivityType.SIGHTSEEING),
            ItineraryActivity("14:30", "Odaiba Seaside", "Waterfront walk \u00b7 Gundam statue", ActivityType.SIGHTSEEING),
            ItineraryActivity("18:00", "Dinner at Gonpachi", "Izakaya \u00b7 Kill Bill restaurant", ActivityType.FOOD)
        )
    ),
    ItineraryDay(
        dayNumber = 5, date = "Mar 18, 2025", city = "Hakone",
        weather = DayWeather(WeatherType.PARTLY_CLOUDY, 16, 8, "Partly cloudy"),
        activities = listOf(
            ItineraryActivity("08:30", "Romancecar to Hakone", "~85 min \u00b7 Scenic route", ActivityType.TRANSIT),
            ItineraryActivity("10:30", "Hakone Open-Air Museum", "Sculpture park \u00b7 \u00a51,600", ActivityType.SIGHTSEEING),
            ItineraryActivity("13:00", "Lunch at Amazake-chaya", "300-year-old teahouse", ActivityType.FOOD),
            ItineraryActivity("15:00", "Owakudani Valley", "Volcanic hot springs \u00b7 Black eggs", ActivityType.SIGHTSEEING),
            ItineraryActivity("17:30", "Return to Tokyo", "Romancecar \u00b7 ~85 min", ActivityType.TRANSIT)
        )
    ),
    ItineraryDay(
        dayNumber = 6, date = "Mar 19, 2025", city = "Kyoto",
        weather = DayWeather(WeatherType.RAINY, 15, 9, "Light rain"),
        activities = listOf(
            ItineraryActivity("07:00", "Shinkansen to Kyoto", "~2 hrs 15 min \u00b7 JR Pass", ActivityType.TRANSIT),
            ItineraryActivity("10:00", "Fushimi Inari Shrine", "Thousand torii gates", ActivityType.SIGHTSEEING),
            ItineraryActivity("12:30", "Lunch at Nishiki Market", "Kyoto\u2019s kitchen \u00b7 Street food", ActivityType.FOOD),
            ItineraryActivity("14:30", "Kinkaku-ji", "Golden Pavilion \u00b7 \u00a5400", ActivityType.SIGHTSEEING)
        )
    ),
    ItineraryDay(
        dayNumber = 7, date = "Mar 20, 2025", city = "Kyoto",
        weather = DayWeather(WeatherType.PARTLY_CLOUDY, 17, 10, "Clearing up"),
        activities = listOf(
            ItineraryActivity("08:00", "Arashiyama Bamboo Grove", "Early morning \u00b7 Less crowded", ActivityType.SIGHTSEEING),
            ItineraryActivity("10:30", "Tenry\u016b-ji Temple", "UNESCO \u00b7 Zen garden \u00b7 \u00a5500", ActivityType.SIGHTSEEING),
            ItineraryActivity("12:30", "Tofu Lunch at Sagano", "Yudofu set \u00b7 ~\u00a52,500", ActivityType.FOOD),
            ItineraryActivity("15:00", "Gion District Walk", "Geisha quarter \u00b7 Tea houses", ActivityType.SIGHTSEEING)
        )
    )
)

// ---------------------------------------------------------------------------
// ItineraryScreen
// ---------------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItineraryScreen() {
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
                onClick = { /* design-only, no action */ },
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
    val (bg, textColor, label) = when (type) {
        ActivityType.FOOD -> Triple(FoodTagBg, FoodTagText, "Food")
        ActivityType.SIGHTSEEING -> Triple(SightTagBg, SightTagText, "Sightseeing")
        ActivityType.TRANSIT -> Triple(TransitTagBg, TransitTagText, "Transit")
    }

    Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.SemiBold,
        color = textColor,
        modifier = Modifier
            .background(bg, RoundedCornerShape(20.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}
