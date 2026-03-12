package com.example.travellikeasigma.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.travellikeasigma.R
import com.example.travellikeasigma.domain.Trip
import com.example.travellikeasigma.ui.components.ConfirmationDialog
import com.example.travellikeasigma.ui.components.TripTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotosScreen(trip: Trip) {
    var selectedIndex: Int by remember { mutableIntStateOf(-1) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* no-op */ },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.photos_add)
                )
            }
        },
        topBar = {
            TripTopAppBar(
                title = stringResource(R.string.photos_title),
                subtitle = stringResource(R.string.photos_subtitle, trip.name, trip.photoCount)
            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            itemsIndexed(trip.photos) { index, photo ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(Color.Black)
                        .clickable { selectedIndex = index },
                    contentAlignment = Alignment.Center
                ) {
                    if (photo.drawableRes != null) {
                        Image(
                            painter = painterResource(photo.drawableRes),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }

    val selected = if (selectedIndex in trip.photos.indices) trip.photos[selectedIndex] else null
    if (selected != null) {
        Dialog(
            onDismissRequest = { selectedIndex = -1 },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    if (selected.drawableRes != null) {
                        Image(
                            painter = painterResource(selected.drawableRes),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = stringResource(R.string.photos_delete),
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { selectedIndex = -1 }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.cd_close),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        ConfirmationDialog(
            title = stringResource(R.string.photos_delete_title),
            message = stringResource(R.string.photos_delete_message),
            confirmText = stringResource(R.string.photos_delete_yes),
            dismissText = stringResource(R.string.photos_delete_no),
            onConfirm = {
                showDeleteDialog = false
                selectedIndex = -1
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}
