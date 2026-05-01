package com.example.travellikeasigma.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travellikeasigma.domain.AccessAction
import com.example.travellikeasigma.domain.AccessLogRepository
import com.example.travellikeasigma.domain.TripRepository
import com.example.travellikeasigma.domain.User
import com.example.travellikeasigma.domain.UserPreferencesRepository
import com.example.travellikeasigma.domain.UserRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val prefsRepo: UserPreferencesRepository,
    private val userRepo: UserRepository,
    private val tripRepo: TripRepository,
    private val accessLogRepo: AccessLogRepository
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
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null && isLoggedIn && !currentUser.isEmailVerified) {
            emailForVerification = currentUser.email ?: ""
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
                val result = Firebase.auth.createUserWithEmailAndPassword(registerEmail, registerPassword).await()
                val firebaseUser = result.user!!
                firebaseUser.updateProfile(userProfileChangeRequest { displayName = registerName }).await()
                firebaseUser.sendEmailVerification().await()
                prefsRepo.login(firebaseUser.email!!, firebaseUser.uid)
                userRepo.saveUser(
                    User(
                        uid = firebaseUser.uid,
                        name = registerName,
                        username = registerUsername,
                        email = firebaseUser.email!!,
                        dateOfBirth = registerDateOfBirth
                    )
                )
                accessLogRepo.logAccess(firebaseUser.uid, AccessAction.LOGIN)
                tripRepo.seedIfEmpty(firebaseUser.uid)
                emailForVerification = firebaseUser.email!!
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
                val result = Firebase.auth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = result.user!!
                firebaseUser.reload().await()
                val freshUser = Firebase.auth.currentUser!!
                val existingUser = userRepo.getUserById(freshUser.uid)
                if (existingUser == null) {
                    Firebase.auth.signOut()
                    authError = noLocalDataMsg
                    return@launch
                }
                prefsRepo.login(freshUser.email!!, freshUser.uid)
                accessLogRepo.logAccess(freshUser.uid, AccessAction.LOGIN)
                tripRepo.seedIfEmpty(freshUser.uid)
                if (!freshUser.isEmailVerified) {
                    emailForVerification = freshUser.email!!
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
                Firebase.auth.currentUser?.reload()?.await()
                if (Firebase.auth.currentUser?.isEmailVerified == true) {
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
                Firebase.auth.currentUser?.reload()?.await()
                if (Firebase.auth.currentUser?.isEmailVerified == true) {
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
                Firebase.auth.currentUser?.sendEmailVerification()?.await()
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
                Firebase.auth.sendPasswordResetEmail(email).await()
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
        Firebase.auth.signOut()
        prefsRepo.logout()
        isLoggedIn = false
        needsEmailVerification = false
        emailForVerification = ""
        email = ""
        password = ""
        clearRegisterFields()
    }
}
