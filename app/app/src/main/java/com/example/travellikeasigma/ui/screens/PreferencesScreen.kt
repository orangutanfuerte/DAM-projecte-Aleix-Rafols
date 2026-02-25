package com.example.travellikeasigma.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.travellikeasigma.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(
    onBackClick:  () -> Unit,
    onTermsClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title          = { Text(stringResource(R.string.pref_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                            modifier           = Modifier.size(18.dp)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Profile header
            ProfileHeader()

            Spacer(modifier = Modifier.height(24.dp))

            // Settings group
            SettingsGroup(label = stringResource(R.string.pref_section_settings)) {
                SettingsRow(
                    icon    = Icons.Filled.Language,
                    label   = stringResource(R.string.pref_language),
                    sublabel = stringResource(R.string.pref_language_current),
                    onClick = { /* language picker — future */ }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp))
                SettingsRow(
                    icon    = Icons.Filled.Notifications,
                    label   = stringResource(R.string.pref_notifications),
                    sublabel = stringResource(R.string.pref_notifications_sub),
                    onClick = { /* notifications — future */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Legal group
            SettingsGroup(label = stringResource(R.string.pref_section_legal)) {
                SettingsRow(
                    icon    = Icons.Filled.Policy,
                    label   = stringResource(R.string.pref_terms),
                    sublabel = stringResource(R.string.pref_terms_sub),
                    onClick = onTermsClick
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp))
                SettingsRow(
                    icon    = Icons.Filled.Info,
                    label   = stringResource(R.string.pref_about),
                    sublabel = stringResource(R.string.pref_about_sub, stringResource(R.string.app_version)),
                    onClick = onAboutClick
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Logout button
            Button(
                onClick  = { /* logout — future */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor   = MaterialTheme.colorScheme.onErrorContainer
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector        = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text       = stringResource(R.string.pref_logout),
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ---------------------------------------------------------------------------
// Profile header — avatar initials, name, email, edit button
// ---------------------------------------------------------------------------
@Composable
private fun ProfileHeader() {
    Column(
        modifier            = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(72.dp),
            shape    = CircleShape,
            color    = MaterialTheme.colorScheme.primaryContainer
        ) {
            Text(
                text      = "S",
                modifier  = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                textAlign = TextAlign.Center,
                style     = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color     = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text       = stringResource(R.string.pref_user_name),
            style      = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text  = stringResource(R.string.pref_user_email),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = { /* edit profile — future */ },
            shape   = RoundedCornerShape(20.dp)
        ) {
            Icon(
                imageVector        = Icons.Filled.Edit,
                contentDescription = null,
                modifier           = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(stringResource(R.string.pref_edit_profile))
        }
    }
}

// ---------------------------------------------------------------------------
// A labelled card group wrapping one or more SettingsRow items
// ---------------------------------------------------------------------------
@Composable
private fun SettingsGroup(
    label:   String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text  = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
        )
        Card(
            shape     = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            content()
        }
    }
}

// ---------------------------------------------------------------------------
// A single tappable row inside a SettingsGroup
// ---------------------------------------------------------------------------
@Composable
private fun SettingsRow(
    icon:     ImageVector,
    label:    String,
    sublabel: String,
    onClick:  () -> Unit
) {
    ListItem(
        modifier     = Modifier.clickable(onClick = onClick),
        leadingContent = {
            Icon(
                imageVector        = icon,
                contentDescription = null,
                tint               = MaterialTheme.colorScheme.primary
            )
        },
        headlineContent = { Text(label) },
        supportingContent = { Text(sublabel) },
        trailingContent = {
            Icon(
                imageVector        = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = null,
                modifier           = Modifier.size(14.dp),
                tint               = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    )
}
