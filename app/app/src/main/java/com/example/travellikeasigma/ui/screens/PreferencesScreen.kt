package com.example.travellikeasigma.ui.screens

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
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.travellikeasigma.R
import com.example.travellikeasigma.ui.components.ConfirmationDialog
import com.example.travellikeasigma.ui.components.ProfileAvatar
import com.example.travellikeasigma.ui.components.TripTopAppBar
import com.example.travellikeasigma.ui.theme.ThemeMode

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun PreferencesScreen(
    onBackClick:           () -> Unit,
    onProfileClick:        () -> Unit,
    onTermsClick:          () -> Unit,
    onAboutClick:          () -> Unit,
    onLogoutClick:         () -> Unit,
    themeMode:             ThemeMode,
    notificationsEnabled:  Boolean,
    language:              String,
    onThemeChange:         (ThemeMode) -> Unit,
    onNotificationsChange: (Boolean) -> Unit,
    onLanguageChange:      (String) -> Unit
) {
    // Dialog visibility state
    var showLanguageDialog      by remember { mutableStateOf(false) }
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var showThemeDialog         by remember { mutableStateOf(false) }
    var showLogoutDialog        by remember { mutableStateOf(false) }

    val selectedLanguage = when (language) {
        "es" -> stringResource(R.string.pref_language_es)
        "ca" -> stringResource(R.string.pref_language_ca)
        else -> stringResource(R.string.pref_language_en)
    }

    val themeSublabel = when (themeMode) {
        ThemeMode.LIGHT  -> stringResource(R.string.pref_theme_light)
        ThemeMode.DARK   -> stringResource(R.string.pref_theme_dark)
        ThemeMode.SYSTEM -> stringResource(R.string.pref_theme_system)
    }

    val notificationsSublabel = if (notificationsEnabled)
        stringResource(R.string.pref_notifications_sub)
    else
        stringResource(R.string.pref_notifications_off)

    // ── Language dialog ─────────────────────────────────────────────────
    if (showLanguageDialog) {
        val languageOptions = listOf(
            "en" to stringResource(R.string.pref_language_en),
            "es" to stringResource(R.string.pref_language_es),
            "ca" to stringResource(R.string.pref_language_ca)
        )
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(stringResource(R.string.pref_language_dialog_title)) },
            text = {
                Column(Modifier.selectableGroup()) {
                    languageOptions.forEach { (code, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (code == language),
                                    onClick  = {
                                        onLanguageChange(code)
                                        showLanguageDialog = false
                                    },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (code == language),
                                onClick  = null
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text(stringResource(android.R.string.cancel))
                }
            }
        )
    }

    // ── Notifications dialog ────────────────────────────────────────────
    if (showNotificationsDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationsDialog = false },
            title = { Text(stringResource(R.string.pref_notifications_dialog_title)) },
            text = {
                Row(
                    modifier          = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text     = stringResource(R.string.pref_notifications),
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked         = notificationsEnabled,
                        onCheckedChange = { onNotificationsChange(it) }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationsDialog = false }) {
                    Text(stringResource(android.R.string.ok))
                }
            }
        )
    }

    // ── Theme dialog ────────────────────────────────────────────────────
    if (showThemeDialog) {
        val options = listOf(
            ThemeMode.LIGHT  to stringResource(R.string.pref_theme_light),
            ThemeMode.DARK   to stringResource(R.string.pref_theme_dark),
            ThemeMode.SYSTEM to stringResource(R.string.pref_theme_system)
        )
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text(stringResource(R.string.pref_theme_dialog_title)) },
            text = {
                Column(Modifier.selectableGroup()) {
                    options.forEach { (mode, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (mode == themeMode),
                                    onClick  = {
                                        onThemeChange(mode)
                                        showThemeDialog = false
                                    },
                                    role = Role.RadioButton
                                )
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (mode == themeMode),
                                onClick  = null
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text(stringResource(android.R.string.cancel))
                }
            }
        )
    }

    // ── Logout confirmation dialog ───────────────────────────────────────
    if (showLogoutDialog) {
        ConfirmationDialog(
            title       = stringResource(R.string.logout_dialog_title),
            message     = stringResource(R.string.logout_dialog_message),
            confirmText = stringResource(R.string.logout_dialog_yes),
            dismissText = stringResource(R.string.logout_dialog_no),
            onConfirm   = { showLogoutDialog = false; onLogoutClick() },
            onDismiss   = { showLogoutDialog = false }
        )
    }

    // ── Screen content ──────────────────────────────────────────────────
    Scaffold(
        topBar = {
            TripTopAppBar(
                title = stringResource(R.string.pref_title),
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(ScrollState(0))
        ) {
            // Profile header
            ProfileHeader(onProfileClick = onProfileClick)

            Spacer(modifier = Modifier.height(24.dp))

            // Settings group
            SettingsGroup(label = stringResource(R.string.pref_section_settings)) {
                SettingsRow(
                    icon     = Icons.Filled.Language,
                    label    = stringResource(R.string.pref_language),
                    sublabel = selectedLanguage,
                    onClick  = { showLanguageDialog = true }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp))
                SettingsRow(
                    icon     = Icons.Filled.Notifications,
                    label    = stringResource(R.string.pref_notifications),
                    sublabel = notificationsSublabel,
                    onClick  = { showNotificationsDialog = true }
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp))
                SettingsRow(
                    icon     = Icons.Filled.DarkMode,
                    label    = stringResource(R.string.pref_theme),
                    sublabel = themeSublabel,
                    onClick  = { showThemeDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Legal group
            SettingsGroup(label = stringResource(R.string.pref_section_legal)) {
                SettingsRow(
                    icon     = Icons.Filled.Policy,
                    label    = stringResource(R.string.pref_terms),
                    sublabel = stringResource(R.string.pref_terms_sub),
                    onClick  = onTermsClick
                )
                HorizontalDivider(modifier = Modifier.padding(start = 56.dp))
                SettingsRow(
                    icon     = Icons.Filled.Info,
                    label    = stringResource(R.string.pref_about),
                    sublabel = stringResource(R.string.pref_about_sub, stringResource(R.string.app_version)),
                    onClick  = onAboutClick
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Logout button
            Button(
                onClick  = { showLogoutDialog = true },
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
private fun ProfileHeader(onProfileClick: () -> Unit) {
    Column(
        modifier            = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileAvatar(
            initials = "S",
            size = 72.dp
        )
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
            onClick = onProfileClick,
            shape   = RoundedCornerShape(20.dp)
        ) {
            Icon(
                imageVector        = Icons.Filled.Person,
                contentDescription = null,
                modifier           = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(stringResource(R.string.pref_view_profile))
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
