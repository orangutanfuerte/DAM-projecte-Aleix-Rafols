package com.example.travellikeasigma.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.travellikeasigma.R
import com.example.travellikeasigma.domain.ApiHotel
import com.example.travellikeasigma.domain.ApiRoom
import com.example.travellikeasigma.domain.Destination
import com.example.travellikeasigma.domain.sampleDestinations
import com.example.travellikeasigma.ui.components.HotelCard
import com.example.travellikeasigma.ui.components.RoomCard
import com.example.travellikeasigma.ui.components.TripTopAppBar
import com.example.travellikeasigma.ui.components.translatedName
import com.example.travellikeasigma.ui.viewmodels.HotelViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Date
import java.util.Locale

private enum class NewTripStep { DESTINATION, NAME, DETAILS, HOTEL }
private enum class HotelSubStep { HOTEL_LIST, ROOM_LIST, BOOKING_FORM }

private val countryFlags = mapOf(
    "Barcelona" to "🇪🇸",
    "London"    to "🇬🇧",
    "Paris"     to "🇫🇷"
)

private fun dateFormatter(locale: Locale) = SimpleDateFormat("MMM d, yyyy", locale)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTripScreen(
    hotelViewModel: HotelViewModel,
    onBackClick: () -> Unit,
    onValidateName: suspend (String) -> Boolean,
    onSave: (name: String, startDate: LocalDate, endDate: LocalDate, destination: Destination) -> Unit
) {
    var currentStep by rememberSaveable { mutableStateOf(NewTripStep.DESTINATION) }

    var selectedCountry by rememberSaveable { mutableStateOf("") }
    var tripName by rememberSaveable { mutableStateOf("") }
    var checkInMillis by rememberSaveable { mutableLongStateOf(-1L) }
    var checkOutMillis by rememberSaveable { mutableLongStateOf(-1L) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    val todayMillis = remember {
        LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    }
    val dateRangeState = rememberDateRangePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long) = utcTimeMillis >= todayMillis
            override fun isSelectableYear(year: Int) = year >= LocalDate.now().year
        }
    )

    val stepTitle = when (currentStep) {
        NewTripStep.DESTINATION -> stringResource(R.string.new_trip_step_destination_title)
        NewTripStep.NAME        -> stringResource(R.string.new_trip_step_name_title)
        NewTripStep.DETAILS     -> stringResource(R.string.new_trip_step_details_title)
        NewTripStep.HOTEL       -> stringResource(R.string.new_trip_step_hotel_title)
    }

    Scaffold(
        topBar = {
            TripTopAppBar(
                title = stepTitle,
                onBackClick = {
                    when (currentStep) {
                        NewTripStep.DESTINATION -> onBackClick()
                        NewTripStep.NAME        -> currentStep = NewTripStep.DESTINATION
                        NewTripStep.DETAILS     -> currentStep = NewTripStep.NAME
                        NewTripStep.HOTEL       -> currentStep = NewTripStep.DETAILS
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
                onValidateName = onValidateName,
                onNext = { currentStep = NewTripStep.DETAILS },
                modifier = Modifier.padding(innerPadding)
            )
            NewTripStep.DETAILS -> DetailsStep(
                checkInMillis = checkInMillis,
                checkOutMillis = checkOutMillis,
                onDateCardClick = { showDatePicker = true },
                onNext = { currentStep = NewTripStep.HOTEL },
                modifier = Modifier.padding(innerPadding)
            )
            NewTripStep.HOTEL -> {
                val startDateStr = remember(checkInMillis) {
                    if (checkInMillis > 0)
                        Instant.ofEpochMilli(checkInMillis).atZone(ZoneOffset.UTC).toLocalDate().toString()
                    else ""
                }
                val endDateStr = remember(checkOutMillis) {
                    if (checkOutMillis > 0)
                        Instant.ofEpochMilli(checkOutMillis).atZone(ZoneOffset.UTC).toLocalDate().toString()
                    else ""
                }
                ApiHotelStep(
                    city = selectedCountry,
                    startDate = startDateStr,
                    endDate = endDateStr,
                    tripName = tripName,
                    hotelViewModel = hotelViewModel,
                    onDone = {
                        val destination = sampleDestinations.find { it.destinationName == selectedCountry }
                            ?: sampleDestinations.first()
                        val start = Instant.ofEpochMilli(checkInMillis).atZone(ZoneOffset.UTC).toLocalDate()
                        val end = Instant.ofEpochMilli(checkOutMillis).atZone(ZoneOffset.UTC).toLocalDate()
                        onSave(tripName, start, end, destination)
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }

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
            items(sampleDestinations) { destination ->
                val isSelected = destination.destinationName == selectedCountry
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCountrySelect(destination.destinationName) },
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
                            text = countryFlags[destination.destinationName] ?: "",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = destination.translatedName(),
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
    onValidateName: suspend (String) -> Boolean,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var isChecking by remember { mutableStateOf(false) }
    var nameError by remember { mutableStateOf<String?>(null) }
    val duplicateError = stringResource(R.string.new_trip_name_duplicate_error)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = tripName,
            onValueChange = {
                onTripNameChange(it)
                nameError = null
            },
            label = { Text(stringResource(R.string.new_trip_name_label)) },
            placeholder = { Text(stringResource(R.string.new_trip_name_placeholder)) },
            isError = nameError != null,
            supportingText = nameError?.let { err -> { Text(err) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                scope.launch {
                    isChecking = true
                    val available = onValidateName(tripName)
                    isChecking = false
                    if (available) {
                        nameError = null
                        onNext()
                    } else {
                        nameError = duplicateError
                    }
                }
            },
            enabled = tripName.isNotBlank() && !isChecking,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.new_trip_cta_next))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Step 3 — Dates
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun DetailsStep(
    checkInMillis: Long,
    checkOutMillis: Long,
    onDateCardClick: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    val datesSelected = checkInMillis > 0 && checkOutMillis > 0
    val fmt = dateFormatter(LocalConfiguration.current.locales[0])
    val checkInText = if (checkInMillis > 0) fmt.format(Date(checkInMillis))
                      else stringResource(R.string.new_trip_select_date)
    val checkOutText = if (checkOutMillis > 0) fmt.format(Date(checkOutMillis))
                       else stringResource(R.string.new_trip_select_date)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
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
            enabled = datesSelected,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.new_trip_cta_next))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Step 4 — Hotel (API)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ApiHotelStep(
    city: String,
    startDate: String,
    endDate: String,
    tripName: String,
    hotelViewModel: HotelViewModel,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    var subStep by remember { mutableStateOf(HotelSubStep.HOTEL_LIST) }
    var selectedHotel by remember { mutableStateOf<ApiHotel?>(null) }
    var selectedRoom by remember { mutableStateOf<ApiRoom?>(null) }
    var guestName by rememberSaveable { mutableStateOf("") }
    var guestEmail by rememberSaveable { mutableStateOf("") }
    var persons by rememberSaveable { mutableIntStateOf(1) }

    LaunchedEffect(Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (guestName.isEmpty()) guestName = user?.displayName ?: ""
        if (guestEmail.isEmpty()) guestEmail = user?.email ?: ""
        hotelViewModel.clearSearch()
        hotelViewModel.searchHotels(city, startDate, endDate)
    }

    when (subStep) {
        HotelSubStep.HOTEL_LIST -> HotelListSubStep(
            hotelViewModel = hotelViewModel,
            onHotelSelect = { hotel ->
                selectedHotel = hotel
                subStep = HotelSubStep.ROOM_LIST
            },
            onSkip = onDone,
            modifier = modifier
        )
        HotelSubStep.ROOM_LIST -> RoomListSubStep(
            hotel = selectedHotel!!,
            onRoomSelect = { room ->
                selectedRoom = room
                subStep = HotelSubStep.BOOKING_FORM
            },
            onBack = { subStep = HotelSubStep.HOTEL_LIST },
            onSkip = onDone,
            modifier = modifier
        )
        HotelSubStep.BOOKING_FORM -> BookingFormSubStep(
            hotel = selectedHotel!!,
            room = selectedRoom!!,
            startDate = startDate,
            endDate = endDate,
            tripName = tripName,
            guestName = guestName,
            onGuestNameChange = { guestName = it },
            guestEmail = guestEmail,
            onGuestEmailChange = { guestEmail = it },
            persons = persons,
            onPersonsChange = { persons = it },
            hotelViewModel = hotelViewModel,
            onBack = { subStep = HotelSubStep.ROOM_LIST },
            onSkip = onDone,
            onBooked = onDone,
            modifier = modifier
        )
    }
}

@Composable
private fun HotelListSubStep(
    hotelViewModel: HotelViewModel,
    onHotelSelect: (ApiHotel) -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        when {
            hotelViewModel.isSearchLoading -> Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            hotelViewModel.searchError != null -> Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.booking_error),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
            hotelViewModel.availableHotels.isEmpty() -> Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.reservations_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
            else -> LazyColumn(
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(hotelViewModel.availableHotels) { hotel ->
                    HotelCard(hotel = hotel, onClick = { onHotelSelect(hotel) })
                }
            }
        }
        OutlinedButton(
            onClick = onSkip,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(stringResource(R.string.booking_skip))
        }
    }
}

@Composable
private fun RoomListSubStep(
    hotel: ApiHotel,
    onRoomSelect: (ApiRoom) -> Unit,
    onBack: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = hotel.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        LazyColumn(
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(hotel.rooms) { room ->
                RoomCard(room = room, onClick = { onRoomSelect(room) })
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.new_trip_cta_back))
            }
            OutlinedButton(onClick = onSkip, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.booking_skip))
            }
        }
    }
}

@Composable
private fun BookingFormSubStep(
    hotel: ApiHotel,
    room: ApiRoom,
    startDate: String,
    endDate: String,
    tripName: String,
    guestName: String,
    onGuestNameChange: (String) -> Unit,
    guestEmail: String,
    onGuestEmailChange: (String) -> Unit,
    persons: Int,
    onPersonsChange: (Int) -> Unit,
    hotelViewModel: HotelViewModel,
    onBack: () -> Unit,
    onSkip: () -> Unit,
    onBooked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "${hotel.name} · ${room.roomType.replaceFirstChar { it.uppercase() }}",
            style = MaterialTheme.typography.titleMedium
        )
        OutlinedTextField(
            value = guestName,
            onValueChange = onGuestNameChange,
            label = { Text(stringResource(R.string.booking_guest_name)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        OutlinedTextField(
            value = guestEmail,
            onValueChange = onGuestEmailChange,
            label = { Text(stringResource(R.string.booking_guest_email)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Text(
            text = stringResource(R.string.booking_persons),
            style = MaterialTheme.typography.labelLarge
        )
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = { if (persons > 1) onPersonsChange(persons - 1) }) { Text("−") }
            Text(text = persons.toString(), style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = { onPersonsChange(persons + 1) }) { Text("+") }
        }

        if (hotelViewModel.bookingError != null) {
            Text(
                text = stringResource(R.string.booking_error),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                hotelViewModel.bookRoom(
                    hotel = hotel,
                    room = room,
                    startDate = startDate,
                    endDate = endDate,
                    guestName = guestName,
                    guestEmail = guestEmail,
                    persons = persons,
                    tripName = tripName.ifBlank { null },
                    onSuccess = { onBooked() },
                    onError = {}
                )
            },
            enabled = guestName.isNotBlank() && guestEmail.isNotBlank() && !hotelViewModel.isBookingLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (hotelViewModel.isBookingLoading) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text(stringResource(R.string.booking_confirm))
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.new_trip_cta_back))
            }
            OutlinedButton(onClick = onSkip, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.booking_skip))
            }
        }
    }
}
