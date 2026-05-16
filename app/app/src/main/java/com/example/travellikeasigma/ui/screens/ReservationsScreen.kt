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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.travellikeasigma.R
import com.example.travellikeasigma.domain.ApiHotel
import com.example.travellikeasigma.domain.ApiRoom
import com.example.travellikeasigma.domain.LocalReservation
import com.example.travellikeasigma.domain.sampleDestinations
import com.example.travellikeasigma.ui.components.HotelCard
import com.example.travellikeasigma.ui.components.RoomCard
import com.example.travellikeasigma.ui.viewmodels.HotelViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneOffset

private enum class BookingStep { SEARCH, HOTELS, ROOMS, FORM, SUCCESS }

private val reservationCountryFlags = mapOf(
    "Barcelona" to "🇪🇸",
    "London"    to "🇬🇧",
    "Paris"     to "🇫🇷"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationsScreen(
    hotelViewModel: HotelViewModel,
    modifier: Modifier = Modifier
) {
    val reservations by hotelViewModel.reservations.collectAsState()
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.reservations_title)) })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showSheet = true }) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        },
        contentWindowInsets = WindowInsets(0),
        modifier = modifier
    ) { innerPadding ->
        if (reservations.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Icon(
                        imageVector = Icons.Filled.Hotel,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = stringResource(R.string.reservations_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Button(onClick = { showSheet = true }) {
                        Text(stringResource(R.string.reservations_new))
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reservations, key = { it.id }) { reservation ->
                    ReservationCard(reservation = reservation)
                }
            }
        }
    }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showSheet = false
                hotelViewModel.clearSearch()
            },
            sheetState = sheetState
        ) {
            BookingBottomSheetContent(
                hotelViewModel = hotelViewModel,
                onDismiss = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        showSheet = false
                        hotelViewModel.clearSearch()
                    }
                }
            )
        }
    }
}

@Composable
private fun ReservationCard(reservation: LocalReservation, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        AsyncImage(
            model = HotelViewModel.BASE_IMAGE_URL + reservation.hotelImageUrl,
            contentDescription = reservation.hotelName,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(120.dp)
        )
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = reservation.hotelName, style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(reservation.hotelRating) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                }
            }
            Text(
                text = reservation.roomType.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${reservation.startDate} → ${reservation.endDate}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "€%.2f / night · %d guests".format(reservation.pricePerNight, reservation.persons),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Booking bottom sheet
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookingBottomSheetContent(
    hotelViewModel: HotelViewModel,
    onDismiss: () -> Unit
) {
    var bookingStep by remember { mutableStateOf(BookingStep.SEARCH) }
    var selectedCity by rememberSaveable { mutableStateOf("") }
    var checkInMillis by rememberSaveable { mutableLongStateOf(-1L) }
    var checkOutMillis by rememberSaveable { mutableLongStateOf(-1L) }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var selectedHotel by remember { mutableStateOf<ApiHotel?>(null) }
    var selectedRoom by remember { mutableStateOf<ApiRoom?>(null) }
    var guestName by rememberSaveable { mutableStateOf("") }
    var guestEmail by rememberSaveable { mutableStateOf("") }
    var persons by rememberSaveable { mutableIntStateOf(1) }

    LaunchedEffect(Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        if (guestName.isEmpty()) guestName = user?.displayName ?: ""
        if (guestEmail.isEmpty()) guestEmail = user?.email ?: ""
    }

    val todayMillis = remember {
        LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    }
    val dateRangeState = rememberDateRangePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long) = utcTimeMillis >= todayMillis
            override fun isSelectableYear(year: Int) = year >= LocalDate.now().year
        }
    )

    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp)) {
        when (bookingStep) {
            BookingStep.SEARCH -> SearchStep(
                selectedCity = selectedCity,
                onCitySelect = { selectedCity = it },
                checkInMillis = checkInMillis,
                checkOutMillis = checkOutMillis,
                onDateCardClick = { showDatePicker = true },
                onSearch = {
                    val start = java.time.Instant.ofEpochMilli(checkInMillis).atZone(ZoneOffset.UTC).toLocalDate().toString()
                    val end = java.time.Instant.ofEpochMilli(checkOutMillis).atZone(ZoneOffset.UTC).toLocalDate().toString()
                    hotelViewModel.clearSearch()
                    hotelViewModel.searchHotels(selectedCity, start, end)
                    bookingStep = BookingStep.HOTELS
                }
            )
            BookingStep.HOTELS -> HotelsStep(
                hotelViewModel = hotelViewModel,
                onHotelSelect = { hotel ->
                    selectedHotel = hotel
                    bookingStep = BookingStep.ROOMS
                },
                onBack = { bookingStep = BookingStep.SEARCH }
            )
            BookingStep.ROOMS -> RoomsStep(
                hotel = selectedHotel!!,
                onRoomSelect = { room ->
                    selectedRoom = room
                    bookingStep = BookingStep.FORM
                },
                onBack = { bookingStep = BookingStep.HOTELS }
            )
            BookingStep.FORM -> FormStep(
                hotel = selectedHotel!!,
                room = selectedRoom!!,
                startDate = java.time.Instant.ofEpochMilli(checkInMillis).atZone(ZoneOffset.UTC).toLocalDate().toString(),
                endDate = java.time.Instant.ofEpochMilli(checkOutMillis).atZone(ZoneOffset.UTC).toLocalDate().toString(),
                guestName = guestName,
                onGuestNameChange = { guestName = it },
                guestEmail = guestEmail,
                onGuestEmailChange = { guestEmail = it },
                persons = persons,
                onPersonsChange = { persons = it },
                hotelViewModel = hotelViewModel,
                onBack = { bookingStep = BookingStep.ROOMS },
                onBooked = { bookingStep = BookingStep.SUCCESS }
            )
            BookingStep.SUCCESS -> SuccessStep(onDone = onDismiss)
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
                modifier = Modifier.fillMaxWidth().weight(1f)
            )
        }
    }
}

@Composable
private fun SearchStep(
    selectedCity: String,
    onCitySelect: (String) -> Unit,
    checkInMillis: Long,
    checkOutMillis: Long,
    onDateCardClick: () -> Unit,
    onSearch: () -> Unit
) {
    val datesSelected = checkInMillis > 0 && checkOutMillis > 0
    val fmt = java.text.SimpleDateFormat("MMM d, yyyy", java.util.Locale.getDefault())

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(stringResource(R.string.hotel_search_title), style = MaterialTheme.typography.titleMedium)

        sampleDestinations.forEach { destination ->
            val isSelected = destination.destinationName == selectedCity
            Card(
                modifier = Modifier.fillMaxWidth().clickable { onCitySelect(destination.destinationName) },
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(text = reservationCountryFlags[destination.destinationName] ?: "", style = MaterialTheme.typography.headlineSmall)
                    Text(text = destination.destinationName, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth().clickable { onDateCardClick() },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column {
                    Text(stringResource(R.string.new_trip_checkin), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = if (checkInMillis > 0) fmt.format(java.util.Date(checkInMillis)) else stringResource(R.string.new_trip_select_date),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Column {
                    Text(stringResource(R.string.new_trip_checkout), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = if (checkOutMillis > 0) fmt.format(java.util.Date(checkOutMillis)) else stringResource(R.string.new_trip_select_date),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Button(
            onClick = onSearch,
            enabled = selectedCity.isNotEmpty() && datesSelected,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.hotel_search_button))
        }
    }
}

@Composable
private fun HotelsStep(
    hotelViewModel: HotelViewModel,
    onHotelSelect: (ApiHotel) -> Unit,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("← ${stringResource(R.string.new_trip_cta_back)}") }
        }
        when {
            hotelViewModel.isSearchLoading -> Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            hotelViewModel.searchError != null -> Text(
                text = stringResource(R.string.booking_error),
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
            hotelViewModel.availableHotels.isEmpty() -> Text(
                text = stringResource(R.string.reservations_empty),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp)
            )
            else -> hotelViewModel.availableHotels.forEach { hotel ->
                HotelCard(hotel = hotel, onClick = { onHotelSelect(hotel) })
            }
        }
    }
}

@Composable
private fun RoomsStep(hotel: ApiHotel, onRoomSelect: (ApiRoom) -> Unit, onBack: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("← ${stringResource(R.string.new_trip_cta_back)}") }
            Text(hotel.name, style = MaterialTheme.typography.titleSmall)
        }
        hotel.rooms.forEach { room ->
            RoomCard(room = room, onClick = { onRoomSelect(room) })
        }
    }
}

@Composable
private fun FormStep(
    hotel: ApiHotel,
    room: ApiRoom,
    startDate: String,
    endDate: String,
    guestName: String,
    onGuestNameChange: (String) -> Unit,
    guestEmail: String,
    onGuestEmailChange: (String) -> Unit,
    persons: Int,
    onPersonsChange: (Int) -> Unit,
    hotelViewModel: HotelViewModel,
    onBack: () -> Unit,
    onBooked: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextButton(onClick = onBack) { Text("← ${stringResource(R.string.new_trip_cta_back)}") }
            Text("${hotel.name} · ${room.roomType.replaceFirstChar { it.uppercase() }}", style = MaterialTheme.typography.titleSmall)
        }
        OutlinedTextField(
            value = guestName, onValueChange = onGuestNameChange,
            label = { Text(stringResource(R.string.booking_guest_name)) },
            modifier = Modifier.fillMaxWidth(), singleLine = true
        )
        OutlinedTextField(
            value = guestEmail, onValueChange = onGuestEmailChange,
            label = { Text(stringResource(R.string.booking_guest_email)) },
            modifier = Modifier.fillMaxWidth(), singleLine = true
        )
        Text(stringResource(R.string.booking_persons), style = MaterialTheme.typography.labelLarge)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = { if (persons > 1) onPersonsChange(persons - 1) }) { Text("−") }
            Text(text = persons.toString(), style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = { onPersonsChange(persons + 1) }) { Text("+") }
        }
        if (hotelViewModel.bookingError != null) {
            Text(stringResource(R.string.booking_error), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        Button(
            onClick = {
                hotelViewModel.bookRoom(
                    hotel = hotel, room = room, startDate = startDate, endDate = endDate,
                    guestName = guestName, guestEmail = guestEmail, persons = persons,
                    onSuccess = { onBooked() }, onError = {}
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
    }
}

@Composable
private fun SuccessStep(onDone: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(Icons.Filled.Hotel, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.primary)
        Text(stringResource(R.string.booking_success), style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center)
        Button(onClick = onDone, modifier = Modifier.fillMaxWidth()) {
            Text("OK")
        }
    }
}
