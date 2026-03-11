package com.example.travellikeasigma.ui.screen

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.travellikeasigma.R
import com.example.travellikeasigma.model.ActivityType
import com.example.travellikeasigma.model.ItineraryActivity
import com.example.travellikeasigma.ui.components.ConfirmationDialog
import com.example.travellikeasigma.ui.components.TripTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditActivityScreen(
    dayNumber: Int,
    activity: ItineraryActivity,
    onBackClick: () -> Unit,
    onUpdate: (ItineraryActivity) -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TripTopAppBar(
                title = stringResource(R.string.edit_activity_title),
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
            initialType = activity.tag ?: ActivityType.OTHERS,
            initialTitle = activity.title,
            initialTime = activity.time,
            initialPrice = if (activity.cost > 0.0) activity.cost.toString() else "",
            initialDescription = activity.subtitle,
            saveButtonText = stringResource(R.string.edit_activity_update),
            onSave = { updatedActivity ->
                onUpdate(updatedActivity.copy(id = activity.id))
            },
            extraContent = {
                Button(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor   = MaterialTheme.colorScheme.onErrorContainer
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(
                        text = stringResource(R.string.edit_activity_delete),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        )
    }

    if (showDeleteDialog) {
        ConfirmationDialog(
            title = stringResource(R.string.edit_activity_delete_title),
            message = stringResource(R.string.edit_activity_delete_message, activity.title),
            confirmText = stringResource(R.string.edit_activity_delete_yes),
            dismissText = stringResource(R.string.edit_activity_delete_no),
            onConfirm = {
                showDeleteDialog = false
                onDelete()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}
