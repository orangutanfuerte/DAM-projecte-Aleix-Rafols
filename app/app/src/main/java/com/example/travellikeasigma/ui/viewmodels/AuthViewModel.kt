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

    var isLoading by mutableStateOf(false)
        private set

    var authError by mutableStateOf<String?>(null)
        private set

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var registerName by mutableStateOf("")
    var registerUsername by mutableStateOf("")
    var registerEmail by mutableStateOf("")
    var registerPassword by mutableStateOf("")
    var registerConfirmPassword by mutableStateOf("")

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
                val profileUpdates = userProfileChangeRequest { displayName = registerName }
                firebaseUser.updateProfile(profileUpdates).await()
                prefsRepo.login(firebaseUser.email!!, firebaseUser.uid)
                userRepo.saveUser(
                    User(
                        uid = firebaseUser.uid,
                        name = registerName,
                        username = registerUsername,
                        email = firebaseUser.email!!,
                        dateOfBirth = ""
                    )
                )
                accessLogRepo.logAccess(firebaseUser.uid, AccessAction.LOGIN)
                tripRepo.seedIfEmpty(firebaseUser.uid, firebaseUser.email!!)
                isLoggedIn = true
            } catch (e: Exception) {
                authError = e.localizedMessage
                Log.e(TAG, "Register failed", e)
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
    }

    fun login() {
        viewModelScope.launch {
            isLoading = true
            authError = null
            try {
                val result = Firebase.auth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = result.user!!
                prefsRepo.login(firebaseUser.email!!, firebaseUser.uid)
                if (userRepo.getUserById(firebaseUser.uid) == null) {
                    userRepo.saveUser(
                        User(
                            uid = firebaseUser.uid,
                            name = firebaseUser.displayName
                                ?: firebaseUser.email!!.substringBefore("@"),
                            username = firebaseUser.email!!.substringBefore("@"),
                            email = firebaseUser.email!!,
                            dateOfBirth = ""
                        )
                    )
                }
                accessLogRepo.logAccess(firebaseUser.uid, AccessAction.LOGIN)
                tripRepo.seedIfEmpty(firebaseUser.uid, firebaseUser.email!!)
                isLoggedIn = true
            } catch (e: Exception) {
                authError = e.localizedMessage
                Log.e(TAG, "Login failed", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun logout() {
        val uid = prefsRepo.getLoggedInUid()
        if (uid != null) {
            viewModelScope.launch { accessLogRepo.logAccess(uid, AccessAction.LOGOUT) }
        }
        Firebase.auth.signOut()
        prefsRepo.logout()
        isLoggedIn = false
        email = ""
        password = ""
    }
}
