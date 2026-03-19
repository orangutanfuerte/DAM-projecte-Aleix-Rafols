package com.example.travellikeasigma.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.travellikeasigma.R
import com.example.travellikeasigma.domain.ActivityType
import com.example.travellikeasigma.domain.ItineraryActivity
import com.example.travellikeasigma.ui.components.label
import com.example.travellikeasigma.ui.components.PickerCard
import com.example.travellikeasigma.ui.components.TripTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityScreen(
    dayNumber: Int,
    onBackClick: () -> Unit,
    onSave: (ItineraryActivity) -> Unit
) {
    Scaffold(
        topBar = {
            TripTopAppBar(
                title = stringResource(R.string.add_activity_title),
                subtitle = stringResource(R.string.add_activity_day_label, dayNumber),
                onBackClick = onBackClick
            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        ActivityForm(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            saveButtonText = stringResource(R.string.add_activity_save),
            onSave = onSave
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityForm(
    modifier: Modifier = Modifier,
    initialType: ActivityType = ActivityType.SIGHTSEEING,
    initialTitle: String = "",
    initialTime: String = "",
    initialPrice: String = "",
    initialDescription: String = "",
    saveButtonText: String,
    onSave: (ItineraryActivity) -> Unit,
    extraContent: @Composable ColumnScope.() -> Unit = {}
) {
    var selectedType by rememberSaveable { mutableStateOf(initialType) }
    var title by rememberSaveable { mutableStateOf(initialTitle) }
    var time by rememberSaveable { mutableStateOf(initialTime) }
    var price by rememberSaveable { mutableStateOf(initialPrice) }
    var description by rememberSaveable { mutableStateOf(initialDescription) }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }

    val timePickerState = rememberTimePickerState(is24Hour = true)

    Column(
        modifier = modifier
            .verticalScroll(ScrollState(0))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Activity type selector
        Text(
            text = stringResource(R.string.add_activity_type),
            style = MaterialTheme.typography.labelLarge
        )
        Row(
            modifier = Modifier.horizontalScroll(ScrollState(0)),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ActivityType.entries.forEach { type ->
                FilterChip(
                    selected = selectedType == type,
                    onClick = { selectedType = type },
                    label = { Text(type.label()) }
                )
            }
        }

        // Title
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text(stringResource(R.string.add_activity_title_label)) },
            placeholder = { Text(stringResource(R.string.add_activity_title_placeholder)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Time
        PickerCard(
            icon = Icons.Filled.AccessTime,
            label = stringResource(R.string.add_activity_time_label),
            value = time,
            placeholder = stringResource(R.string.add_activity_time_placeholder),
            onClick = { showTimePicker = true },
            modifier = Modifier.fillMaxWidth()
        )

        // Price
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text(stringResource(R.string.add_activity_price_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            suffix = { Text("\u20ac") }
        )

        // Description
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(stringResource(R.string.add_activity_description_label)) },
            placeholder = { Text(stringResource(R.string.add_activity_description_placeholder)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Save / Update button
        Button(
            onClick = {
                val activity = ItineraryActivity(
                    id = 0,
                    time = time,
                    title = title,
                    subtitle = description,
                    cost = price.toDoubleOrNull() ?: 0.0,
                    tag = if (selectedType == ActivityType.OTHERS) null else selectedType
                )
                onSave(activity)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = title.isNotBlank() && time.isNotBlank(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(saveButtonText)
        }

        extraContent()
    }

    // TimePicker dialog
    if (showTimePicker) {
        Dialog(onDismissRequest = { showTimePicker = false }) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.add_activity_time_picker_title),
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )
                    TimePicker(state = timePickerState)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showTimePicker = false }) {
                            Text(stringResource(R.string.add_activity_time_cancel))
                        }
                        TextButton(onClick = {
                            time = "%02d:%02d".format(
                                timePickerState.hour,
                                timePickerState.minute
                            )
                            showTimePicker = false
                        }) {
                            Text(stringResource(R.string.add_activity_time_confirm))
                        }
                    }
                }
            }
        }
    }
}
