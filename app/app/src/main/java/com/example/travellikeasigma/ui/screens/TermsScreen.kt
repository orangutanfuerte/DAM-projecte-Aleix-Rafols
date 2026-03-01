package com.example.travellikeasigma.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.travellikeasigma.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsScreen(onBackClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.terms_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back)
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
                .verticalScroll(ScrollState(0))
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // Each section: heading + body
            val sections = listOf(
                R.string.terms_s1_title to R.string.terms_s1_body,
                R.string.terms_s2_title to R.string.terms_s2_body,
                R.string.terms_s3_title to R.string.terms_s3_body,
                R.string.terms_s4_title to R.string.terms_s4_body,
                R.string.terms_s5_title to R.string.terms_s5_body,
                R.string.terms_s6_title to R.string.terms_s6_body,
                R.string.terms_s7_title to R.string.terms_s7_body
            )

            sections.forEach { (titleRes, bodyRes) ->
                Text(
                    text       = stringResource(titleRes),
                    style      = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text  = stringResource(bodyRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
