package com.example.travellikeasigma.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.travellikeasigma.domain.UserPreferencesRepository
import com.example.travellikeasigma.ui.theme.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val repo: UserPreferencesRepository
) : ViewModel() {

    var themeMode by mutableStateOf(mapStringToThemeMode(repo.getThemeMode()))
        private set

    var notificationsEnabled by mutableStateOf(repo.isNotificationsEnabled())
        private set

    fun updateThemeMode(mode: ThemeMode) {
        themeMode = mode
        repo.setThemeMode(mode.name.lowercase())
    }

    fun updateNotificationsEnabled(enabled: Boolean) {
        notificationsEnabled = enabled
        repo.setNotificationsEnabled(enabled)
    }

    private fun mapStringToThemeMode(value: String): ThemeMode = when (value) {
        "light" -> ThemeMode.LIGHT
        "dark"  -> ThemeMode.DARK
        else    -> ThemeMode.SYSTEM
    }
}
