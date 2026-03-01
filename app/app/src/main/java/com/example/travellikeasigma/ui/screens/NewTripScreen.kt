package com.example.travellikeasigma.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.travellikeasigma.R
import com.example.travellikeasigma.model.NewTripInput
import com.example.travellikeasigma.model.sampleCountries
import com.example.travellikeasigma.model.sampleHotels
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private enum class NewTripStep { DESTINATION, NAME, DETAILS, HOTEL }

private val countryFlags = mapOf(
    "Japan"   to "🇯🇵",
    "Iceland" to "🇮🇸",
    "Italy"   to "🇮🇹",
    "Spain"   to "🇪🇸",
    "Brasil"  to "🇧🇷"
)

private val dateFormatter = SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTripScreen(
    onBackClick: () -> Unit,
    onSave: (NewTripInput) -> Unit
) {
    var currentStep by rememberSaveable { mutableStateOf(NewTripStep.DESTINATION) }

    // Step 1 state
    var selectedCountry by rememberSaveable { mutableStateOf("") }

    // Step 2 state
    var tripName by rememberSaveable { mutableStateOf("") }

    // Step 3 state
    var persons by rememberSaveable { mutableIntStateOf(1) }
    var checkInMillis by rememberSaveable { mutableLongStateOf(-1L) }
    var checkOutMillis by rememberSaveable { mutableLongStateOf(-1L) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    // Step 4 state — store ID only (Int is Bundle-safe)
    var selectedHotelId by rememberSaveable { mutableIntStateOf(-1) }
    val selectedHotel = sampleHotels.find { it.id == selectedHotelId }

    // DateRangePicker state lives outside the dialog so it persists while open
    val dateRangeState = rememberDateRangePickerState()

    val stepTitle = when (currentStep) {
        NewTripStep.DESTINATION -> stringResource(R.string.new_trip_step_destination_title)
        NewTripStep.NAME        -> stringResource(R.string.new_trip_step_name_title)
        NewTripStep.DETAILS     -> stringResource(R.string.new_trip_step_details_title)
        NewTripStep.HOTEL       -> stringResource(R.string.new_trip_step_hotel_title)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stepTitle) },
                navigationIcon = {
                    IconButton(onClick = {
                        when (currentStep) {
                            NewTripStep.DESTINATION -> onBackClick()
                            NewTripStep.NAME        -> currentStep = NewTripStep.DESTINATION
                            NewTripStep.DETAILS     -> currentStep = NewTripStep.NAME
                            NewTripStep.HOTEL       -> currentStep = NewTripStep.DETAILS
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back)
                        )
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        when (currentStep) {
            NewTripStep.DESTINATION -> DestinationStep(
                selectedCountry = selectedCountry,
                onCountrySelect = { selectedCountry = it },
                onNext = { currentStep = NewTripStep.NAME },
                modifier = Modifier.padding(innerPadding)
            )
            NewTripStep.NAME -> NameStep(
                tripName = tripName,
                onTripNameChange = { tripName = it },
                onNext = { currentStep = NewTripStep.DETAILS },
                modifier = Modifier.padding(innerPadding)
            )
            NewTripStep.DETAILS -> DetailsStep(
                persons = persons,
                onPersonsChange = { persons = it },
                checkInMillis = checkInMillis,
                checkOutMillis = checkOutMillis,
                onDateCardClick = { showDatePicker = true },
                onNext = { currentStep = NewTripStep.HOTEL },
                modifier = Modifier.padding(innerPadding)
            )
            NewTripStep.HOTEL -> HotelStep(
                selectedHotelId = selectedHotelId,
                onHotelSelect = { selectedHotelId = it.id },
                onSave = {
                    onSave(
                        NewTripInput(
                            destination    = selectedCountry,
                            tripName       = tripName,
                            persons        = persons,
                            checkInMillis  = checkInMillis,
                            checkOutMillis = checkOutMillis,
                            hotel          = selectedHotel!!
                        )
                    )
                },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    // DateRangePicker dialog — title/headline suppressed to maximise calendar space
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dateRangeState.selectedStartDateMillis?.let { checkInMillis = it }
                    dateRangeState.selectedEndDateMillis?.let { checkOutMillis = it }
                    showDatePicker = false
                }) {
                    Text(stringResource(R.string.new_trip_date_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.new_trip_date_cancel))
                }
            }
        ) {
            DateRangePicker(
                state = dateRangeState,
                title = null,
                headline = null,
                showModeToggle = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Step 1 — Destination
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun DestinationStep(
    selectedCountry: String,
    onCountrySelect: (String) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sampleCountries) { country ->
                val isSelected = country == selectedCountry
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCountrySelect(country) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = countryFlags[country] ?: "",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = country,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
        Button(
            onClick = onNext,
            enabled = selectedCountry.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.new_trip_cta_next))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Step 2 — Trip Name
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun NameStep(
    tripName: String,
    onTripNameChange: (String) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = tripName,
            onValueChange = onTripNameChange,
            label = { Text(stringResource(R.string.new_trip_name_label)) },
            placeholder = { Text(stringResource(R.string.new_trip_name_placeholder)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNext,
            enabled = tripName.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.new_trip_cta_next))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Step 3 — Travellers & Dates
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun DetailsStep(
    persons: Int,
    onPersonsChange: (Int) -> Unit,
    checkInMillis: Long,
    checkOutMillis: Long,
    onDateCardClick: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val datesSelected = checkInMillis > 0 && checkOutMillis > 0
    val checkInText = if (checkInMillis > 0)
        dateFormatter.format(Date(checkInMillis))
    else
        stringResource(R.string.new_trip_select_date)
    val checkOutText = if (checkOutMillis > 0)
        dateFormatter.format(Date(checkOutMillis))
    else
        stringResource(R.string.new_trip_select_date)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Persons counter
        Text(
            text = stringResource(R.string.new_trip_persons_label),
            style = MaterialTheme.typography.labelLarge
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = { if (persons > 1) onPersonsChange(persons - 1) }) {
                Icon(Icons.Filled.Remove, contentDescription = null)
            }
            Text(
                text = persons.toString(),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
            )
            IconButton(onClick = { onPersonsChange(persons + 1) }) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }

        // Date card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onDateCardClick() },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.new_trip_checkin),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = checkInText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (checkInMillis > 0) MaterialTheme.colorScheme.onSurface
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Column {
                        Text(
                            text = stringResource(R.string.new_trip_checkout),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = checkOutText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (checkOutMillis > 0) MaterialTheme.colorScheme.onSurface
                                    else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNext,
            enabled = persons >= 1 && datesSelected,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.new_trip_cta_next))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Step 4 — Hotel
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun HotelStep(
    selectedHotelId: Int,
    onHotelSelect: (com.example.travellikeasigma.model.Hotel) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sampleHotels) { hotel ->
                val isSelected = hotel.id == selectedHotelId
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onHotelSelect(hotel) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = hotel.name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "€%.2f %s".format(hotel.pricePerNight, stringResource(R.string.new_trip_per_night)),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        Button(
            onClick = onSave,
            enabled = selectedHotelId >= 0,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.new_trip_cta_save))
        }
    }
}
