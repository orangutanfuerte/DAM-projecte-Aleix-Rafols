package com.example.travellikeasigma.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.travellikeasigma.domain.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: UserPreferencesRepository
) : ViewModel() {

    var isLoggedIn by mutableStateOf(repo.isLoggedIn())
        private set

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var loginError by mutableStateOf(false)
        private set

    fun login(): Boolean {
        if (email == TEST_EMAIL && password == TEST_PASSWORD) {
            repo.login(email)
            isLoggedIn = true
            loginError = false
            return true
        }
        loginError = true
        return false
    }

    fun logout() {
        repo.logout()
        isLoggedIn = false
        email = ""
        password = ""
    }

    companion object {
        private const val TEST_EMAIL = "sofia.martinez@email.com"
        private const val TEST_PASSWORD = "1234"
    }
}
