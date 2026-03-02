package com.example.travellikeasigma.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.travellikeasigma.R
import com.example.travellikeasigma.model.Trip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotosScreen(trip: Trip) {
    var selectedIndex: Int by remember { mutableIntStateOf(-1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.photos_title),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = stringResource(R.string.photos_subtitle, trip.name, trip.photoCount),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
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
                        .background(photo.color)
                        .clickable { selectedIndex = index },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Image,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = photo.color.copy(
                            red = (photo.color.red * 0.6f).coerceIn(0f, 1f),
                            green = (photo.color.green * 0.6f).coerceIn(0f, 1f),
                            blue = (photo.color.blue * 0.6f).coerceIn(0f, 1f)
                        )
                    )
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
                    modifier = Modifier.fillMaxSize().background(selected.color),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Image,
                        contentDescription = null,
                        modifier = Modifier.size(96.dp),
                        tint = selected.color.copy(
                            red = (selected.color.red * 0.6f).coerceIn(0f, 1f),
                            green = (selected.color.green * 0.6f).coerceIn(0f, 1f),
                            blue = (selected.color.blue * 0.6f).coerceIn(0f, 1f)
                        )
                    )
                }
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = { /* no-op */ }) {
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
}
