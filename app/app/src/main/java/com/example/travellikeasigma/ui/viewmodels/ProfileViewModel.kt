package com.example.travellikeasigma.ui.viewmodels

import android.util.Log
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

    companion object {
        private const val TAG = "ProfileViewModel"
    }

    var user by mutableStateOf<User?>(null)
        private set

    var editPhone by mutableStateOf("")
    var editAddress by mutableStateOf("")
    var editCountry by mutableStateOf("")
    var editAcceptsEmails by mutableStateOf(false)
    var isSaving by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            val uid = prefsRepo.getLoggedInUid() ?: return@launch
            user = userRepo.getUserById(uid)
            user?.let { syncEditFields(it) }
        }
    }

    fun reload() {
        viewModelScope.launch {
            val uid = prefsRepo.getLoggedInUid() ?: return@launch
            user = userRepo.getUserById(uid)
            user?.let { syncEditFields(it) }
        }
    }

    fun saveEditedProfile() {
        viewModelScope.launch {
            isSaving = true
            try {
                val existing = user ?: return@launch
                val updated = existing.copy(
                    phone = editPhone,
                    address = editAddress,
                    country = editCountry,
                    acceptsReceiveEmails = editAcceptsEmails
                )
                userRepo.updateUser(updated)
                user = updated
            } catch (e: Exception) {
                Log.e(TAG, "saveEditedProfile failed", e)
            } finally {
                isSaving = false
            }
        }
    }

    private fun syncEditFields(u: User) {
        editPhone = u.phone ?: ""
        editAddress = u.address ?: ""
        editCountry = u.country ?: ""
        editAcceptsEmails = u.acceptsReceiveEmails
    }
}
