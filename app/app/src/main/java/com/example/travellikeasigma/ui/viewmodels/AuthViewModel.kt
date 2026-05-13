package com.example.travellikeasigma.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travellikeasigma.domain.AccessAction
import com.example.travellikeasigma.domain.AccessLogRepository
import com.example.travellikeasigma.domain.AuthRepository
import com.example.travellikeasigma.domain.TripRepository
import com.example.travellikeasigma.domain.User
import com.example.travellikeasigma.domain.UserPreferencesRepository
import com.example.travellikeasigma.domain.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val prefsRepo: UserPreferencesRepository,
    private val userRepo: UserRepository,
    private val tripRepo: TripRepository,
    private val accessLogRepo: AccessLogRepository,
    private val authRepo: AuthRepository
) : ViewModel() {

    companion object {
        private const val TAG = "AuthViewModel"
    }

    var isLoggedIn by mutableStateOf(prefsRepo.isLoggedIn())
        private set

    var needsEmailVerification by mutableStateOf(false)
        private set

    var verificationError by mutableStateOf<String?>(null)
        private set

    var emailForVerification by mutableStateOf("")
        private set

    init {
        val currentUser = authRepo.getCurrentUser()
        if (currentUser != null && isLoggedIn && !currentUser.isEmailVerified) {
            emailForVerification = currentUser.email
            needsEmailVerification = true
        }
    }

    var resetPasswordSent by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var authError by mutableStateOf<String?>(null)

    // Login fields
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    // Register fields
    var registerName by mutableStateOf("")
    var registerUsername by mutableStateOf("")
    var registerEmail by mutableStateOf("")
    var registerPassword by mutableStateOf("")
    var registerConfirmPassword by mutableStateOf("")
    var registerDateOfBirth by mutableStateOf("")

    // Profile completion fields
    var profilePhone by mutableStateOf("")
    var profileAddress by mutableStateOf("")
    var profileCountry by mutableStateOf("")
    var profileAcceptsEmails by mutableStateOf(false)

    fun register(usernameAlreadyTakenMsg: String) {
        viewModelScope.launch {
            isLoading = true
            authError = null
            try {
                if (userRepo.isUsernameTaken(registerUsername)) {
                    authError = usernameAlreadyTakenMsg
                    isLoading = false
                    return@launch
                }
                val authUser = authRepo.register(registerEmail, registerPassword, registerName)
                prefsRepo.login(authUser.email, authUser.uid)
                userRepo.saveUser(
                    User(
                        uid = authUser.uid,
                        name = registerName,
                        username = registerUsername,
                        email = authUser.email,
                        dateOfBirth = registerDateOfBirth
                    )
                )
                accessLogRepo.logAccess(authUser.uid, AccessAction.LOGIN)
                tripRepo.seedIfEmpty(authUser.uid)
                emailForVerification = authUser.email
                needsEmailVerification = true
                isLoggedIn = true
            } catch (e: Exception) {
                authError = e.localizedMessage
                Log.e(TAG, "Register failed", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun completeProfile() {
        viewModelScope.launch {
            isLoading = true
            try {
                val uid = prefsRepo.getLoggedInUid() ?: return@launch
                val existing = userRepo.getUserById(uid) ?: return@launch
                userRepo.updateUser(
                    existing.copy(
                        phone = profilePhone,
                        address = profileAddress,
                        country = profileCountry,
                        acceptsReceiveEmails = profileAcceptsEmails
                    )
                )
            } catch (e: Exception) {
                Log.e(TAG, "completeProfile failed", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun clearRegisterFields() {
        registerName = ""
        registerUsername = ""
        registerEmail = ""
        registerPassword = ""
        registerConfirmPassword = ""
        registerDateOfBirth = ""
        authError = null
    }

    fun login(noLocalDataMsg: String) {
        viewModelScope.launch {
            isLoading = true
            authError = null
            try {
                val authUser = authRepo.login(email, password)
                val existingUser = userRepo.getUserById(authUser.uid)
                if (existingUser == null) {
                    authRepo.signOut()
                    authError = noLocalDataMsg
                    return@launch
                }
                prefsRepo.login(authUser.email, authUser.uid)
                accessLogRepo.logAccess(authUser.uid, AccessAction.LOGIN)
                tripRepo.seedIfEmpty(authUser.uid)
                if (!authUser.isEmailVerified) {
                    emailForVerification = authUser.email
                    needsEmailVerification = true
                }
                isLoggedIn = true
            } catch (e: Exception) {
                authError = e.localizedMessage
                Log.e(TAG, "Login failed", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun checkEmailVerified() {
        viewModelScope.launch {
            try {
                authRepo.reloadCurrentUser()
                if (authRepo.getCurrentUser()?.isEmailVerified == true) {
                    needsEmailVerification = false
                }
            } catch (e: Exception) {
                Log.e(TAG, "checkEmailVerified failed", e)
            }
        }
    }

    fun checkEmailVerifiedManual(notVerifiedMsg: String) {
        viewModelScope.launch {
            verificationError = null
            try {
                authRepo.reloadCurrentUser()
                if (authRepo.getCurrentUser()?.isEmailVerified == true) {
                    needsEmailVerification = false
                } else {
                    verificationError = notVerifiedMsg
                }
            } catch (e: Exception) {
                Log.e(TAG, "checkEmailVerified failed", e)
            }
        }
    }

    fun sendVerificationEmail(tooManyRequestsMsg: String) {
        viewModelScope.launch {
            verificationError = null
            try {
                authRepo.sendEmailVerification()
            } catch (e: Exception) {
                verificationError = tooManyRequestsMsg
                Log.e(TAG, "sendVerificationEmail failed", e)
            }
        }
    }

    fun sendPasswordReset(emptyEmailMsg: String) {
        viewModelScope.launch {
            if (email.isBlank()) {
                authError = emptyEmailMsg
                return@launch
            }
            authError = null
            try {
                authRepo.sendPasswordResetEmail(email)
                resetPasswordSent = true
            } catch (e: Exception) {
                authError = e.localizedMessage
            }
        }
    }

    fun dismissResetPasswordDialog() {
        resetPasswordSent = false
    }

    fun logout() {
        val uid = prefsRepo.getLoggedInUid()
        if (uid != null) {
            viewModelScope.launch { accessLogRepo.logAccess(uid, AccessAction.LOGOUT) }
        }
        authRepo.signOut()
        prefsRepo.logout()
        isLoggedIn = false
        needsEmailVerification = false
        emailForVerification = ""
        email = ""
        password = ""
        clearRegisterFields()
    }
}
