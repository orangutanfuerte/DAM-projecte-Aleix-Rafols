package com.example.travellikeasigma.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travellikeasigma.domain.User
import com.example.travellikeasigma.domain.UserPreferencesRepository
import com.example.travellikeasigma.domain.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val prefsRepo: UserPreferencesRepository
) : ViewModel() {

    var user by mutableStateOf<User?>(null)
        private set

    init {
        viewModelScope.launch {
            val uid = prefsRepo.getLoggedInUid() ?: return@launch
            user = userRepo.getUserById(uid)
        }
    }
}
