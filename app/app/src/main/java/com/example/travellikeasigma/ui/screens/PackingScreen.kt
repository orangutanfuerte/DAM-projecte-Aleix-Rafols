package com.example.travellikeasigma.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.travellikeasigma.R
import com.example.travellikeasigma.model.PackingCategory
import com.example.travellikeasigma.model.samplePackingList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackingScreen() {
    val allItems = samplePackingList.categories.flatMap { it.items }
    val totalCount = allItems.size

    // Track packed item IDs — Set<Int> is Bundle-safe
    val initialPacked = allItems.filter { it.isPacked }.map { it.id }.toSet()
    var packedIds by rememberSaveable { mutableStateOf(initialPacked) }
    val packedCount = packedIds.size

    // One input text state per category — SnapshotStateList so only the changed
    // index triggers recomposition, not the entire list (layout-only, no-op buttons)
    val addItemTexts: SnapshotStateList<String> = remember {
        List(samplePackingList.categories.size) { "" }.toMutableStateList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.packing_title)) }
            )
        },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // ── Overall progress ──────────────────────────────────────────────
            item {
                ProgressCard(packed = packedCount, total = totalCount)
            }

            // ── Categories ───────────────────────────────────────────────────
            samplePackingList.categories.forEachIndexed { index, category ->
                item(key = "header_${category.id}") {
                    Spacer(Modifier.height(8.dp))
                    CategoryHeader(
                        category = category,
                        packedCount = category.items.count { it.id in packedIds }
                    )
                }

                items(
                    items = category.items,
                    key = { it.id }
                ) { packingItem ->
                    PackingItemRow(
                        name = packingItem.name,
                        isPacked = packingItem.id in packedIds,
                        onToggle = {
                            packedIds = if (packingItem.id in packedIds)
                                packedIds - packingItem.id
                            else
                                packedIds + packingItem.id
                        }
                    )
                }

                item(key = "add_item_$index") {
                    AddItemRow(
                        text = addItemTexts[index],
                        onTextChange = { addItemTexts[index] = it }
                    )
                }
            }

            // ── Add category ─────────────────────────────────────────────────
            item {
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { /* no-op */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(stringResource(R.string.packing_add_category))
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Progress card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ProgressCard(packed: Int, total: Int) {
    val fraction = if (total > 0) packed / total.toFloat() else 0f
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.packing_progress, packed, total),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            LinearProgressIndicator(
                progress = { fraction },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Category header
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun CategoryHeader(category: PackingCategory, packedCount: Int) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${category.emoji}  ${category.name}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$packedCount/${category.items.size}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        HorizontalDivider(modifier = Modifier.padding(top = 4.dp))
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Item row
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun PackingItemRow(
    name: String,
    isPacked: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isPacked,
            onCheckedChange = null   // row handles the click
        )
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            textDecoration = if (isPacked) TextDecoration.LineThrough else TextDecoration.None,
            color = if (isPacked)
                MaterialTheme.colorScheme.onSurfaceVariant
            else
                MaterialTheme.colorScheme.onSurface
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Add item row (layout only — button does nothing)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AddItemRow(
    text: String,
    onTextChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(4.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (text.isEmpty()) {
                Text(
                    text = stringResource(R.string.packing_add_item_placeholder),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            BasicTextField(
                value = text,
                onValueChange = onTextChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        IconButton(onClick = { /* no-op */ }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(R.string.packing_add_item)
            )
        }
    }
}
