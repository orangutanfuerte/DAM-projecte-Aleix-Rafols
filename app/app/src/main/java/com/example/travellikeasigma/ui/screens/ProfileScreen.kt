package com.example.travellikeasigma.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.travellikeasigma.R
import com.example.travellikeasigma.domain.User
import com.example.travellikeasigma.ui.components.ProfileAvatar
import com.example.travellikeasigma.ui.components.TripTopAppBar

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User?,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TripTopAppBar(
                title = stringResource(R.string.profile_title),
                onBackClick = onBackClick,
                actions = {
                    IconButton(onClick = onEditClick) {
                        Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.pref_edit_profile))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            ProfileAvatar(
                initials = user?.name?.firstOrNull()?.uppercase() ?: "?",
                size = 80.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = user?.name ?: stringResource(R.string.profile_empty),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = user?.email ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(28.dp))

            ProfileSection(label = stringResource(R.string.profile_section_personal)) {
                ProfileField(
                    label = stringResource(R.string.profile_field_name),
                    value = user?.name
                )
                HorizontalDivider(modifier = Modifier.padding(start = 16.dp))
                ProfileField(
                    label = stringResource(R.string.profile_field_username),
                    value = user?.username
                )
                HorizontalDivider(modifier = Modifier.padding(start = 16.dp))
                ProfileField(
                    label = stringResource(R.string.profile_field_email),
                    value = user?.email
                )
                HorizontalDivider(modifier = Modifier.padding(start = 16.dp))
                ProfileField(
                    label = stringResource(R.string.profile_field_dob),
                    value = user?.dateOfBirth
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ProfileSection(label = stringResource(R.string.profile_section_contact)) {
                ProfileField(
                    label = stringResource(R.string.profile_field_phone),
                    value = user?.phone
                )
                HorizontalDivider(modifier = Modifier.padding(start = 16.dp))
                ProfileField(
                    label = stringResource(R.string.profile_field_address),
                    value = user?.address
                )
                HorizontalDivider(modifier = Modifier.padding(start = 16.dp))
                ProfileField(
                    label = stringResource(R.string.profile_field_country),
                    value = user?.country
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            ProfileSection(label = stringResource(R.string.profile_section_comms)) {
                ProfileField(
                    label = stringResource(R.string.profile_field_accepts_emails),
                    value = if (user?.acceptsReceiveEmails == true)
                        stringResource(R.string.profile_yes)
                    else
                        stringResource(R.string.profile_no)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ProfileSection(
    label: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 6.dp, start = 4.dp)
        )
        Card(
            shape = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            content()
        }
    }
}

@Composable
private fun ProfileField(label: String, value: String?) {
    val display = if (value.isNullOrBlank()) stringResource(R.string.profile_empty) else value
    ListItem(
        headlineContent = { Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        supportingContent = { Text(display, style = MaterialTheme.typography.bodyLarge) }
    )
}
